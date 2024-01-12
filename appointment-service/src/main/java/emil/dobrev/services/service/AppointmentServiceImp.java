package emil.dobrev.services.service;

import emil.dobrev.services.dto.*;
import emil.dobrev.services.exception.DoctorIsNotAvailableAtThisTimeSlotException;
import emil.dobrev.services.exception.NotFoundException;
import emil.dobrev.services.exception.NotValidWorkingDayException;
import emil.dobrev.services.exception.UnauthorizedException;
import emil.dobrev.services.model.Appointment;
import emil.dobrev.services.model.DoctorSchedule;
import emil.dobrev.services.repository.AppointmentRepository;
import emil.dobrev.services.repository.DoctorScheduleRepository;
import emil.dobrev.services.service.interfaces.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static emil.dobrev.services.model.NationalHolidaysInGermany.nationalHolidaysInGermany;
import static emil.dobrev.services.shared.PermissionsUtils.checkForPatientOrDoctorPermission;
import static emil.dobrev.services.shared.PermissionsUtils.checkForPatientPermission;


@Service
@RequiredArgsConstructor
public class AppointmentServiceImp implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final KafkaService kafkaService;

    @Override
    public AppointmentResponse create(CreateAppointmentRequest createAppointmentRequest, Long patientId, String roles) {
        checkForPatientPermission(roles);
        var now  = java.time.LocalDate.now();
        var requestedDateTime = createAppointmentRequest.appointmentDateTime();
        var endOfAppointment = requestedDateTime.plusMinutes(30);
        var doctorId = createAppointmentRequest.doctorId();

        if(requestedDateTime.toLocalDate().isBefore(now)) {
            throw new DoctorIsNotAvailableAtThisTimeSlotException("Appointment time can't be in the past");
        }

        var doctorSchedule = doctorScheduleRepository.findByDoctorId(doctorId).orElseThrow(() -> new NotFoundException("No schedule for doctor with doctorId:" + doctorId));
        var holidays = doctorScheduleRepository.getAllVacationsForDoctor(doctorSchedule.getId());
        var appointments = appointmentRepository.findAppointmentsByDoctorIdAndAppointmentDateTimeGreaterThanEqualAndAppointmentDateTimeLessThanEqual(doctorId, requestedDateTime, endOfAppointment);


        if (!isDoctorAvailable(createAppointmentRequest.appointmentDateTime(), createAppointmentRequest.doctorId(), appointments)) {
            throw new DoctorIsNotAvailableAtThisTimeSlotException(doctorId, requestedDateTime);
        }

        if (!isValidWorkingDayForDoctor(doctorSchedule, holidays.orElse(Collections.emptyList()), requestedDateTime.toLocalDate())) {
            throw new NotValidWorkingDayException(doctorId);
        }

        var appointment = Appointment.builder().appointmentDateTime(requestedDateTime).endOFAppointmentDateTime(endOfAppointment).doctorId(doctorId).patientId(patientId).build();

        appointment = appointmentRepository.save(appointment);

        kafkaService.sendAppointmentNotification(appointment);

        return new AppointmentResponse(appointment.getAppointmentDateTime(), appointment.getEndOFAppointmentDateTime());
    }

    @Override
    public List<TimeSlot> getAllAvailableSlots(Long doctorId, String roles, LocalDate requestedDate) {
        checkForPatientOrDoctorPermission(roles);
        var doctorSchedule = doctorScheduleRepository.findByDoctorId(doctorId).orElseThrow(() -> new NotFoundException("No schedule for doctor with doctorId:" + doctorId));
        var holidays = doctorScheduleRepository.getAllVacationsForDoctor(doctorSchedule.getId());

        if (!isValidWorkingDayForDoctor(doctorSchedule, holidays.orElse(Collections.emptyList()), requestedDate)) {
            throw new NotValidWorkingDayException(doctorId);
        }

        List<TimeSlot> availableSlots = new ArrayList<>();
        LocalTime startTime = doctorSchedule.getStartTime();
        LocalTime endTime = doctorSchedule.getEndTime();
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDate today = currentDate.toLocalDate();
        LocalTime currentTime = currentDate.toLocalTime();

        List<Appointment> appointments = appointmentRepository.findAppointmentsByDoctorIdAndAppointmentDateTimeBetween(doctorId, LocalDateTime.of(requestedDate, startTime), LocalDateTime.of(requestedDate, endTime));

        boolean isRequestedDateIsToday = requestedDate.equals(today);

        LocalDateTime currentSlotStart = LocalDateTime.of(today, startTime);
        //takes available slots until end of the shift from doctor schedule
        while (currentSlotStart.toLocalTime().isBefore(endTime)) {
            LocalDateTime currentSlotEnd = currentSlotStart.plusMinutes(30);
            LocalDateTime finalCurrentSlotStart = currentSlotStart;
            boolean isSlotAvailable = appointments.stream().noneMatch(appointment -> isTimeBetween(finalCurrentSlotStart.toLocalTime(), currentSlotEnd.toLocalTime(), appointment.getAppointmentDateTime().toLocalTime(), appointment.getEndOFAppointmentDateTime().toLocalTime()));


            if (isSlotAvailable && !isTimeBetween(currentSlotStart.toLocalTime(), currentSlotEnd.toLocalTime(), doctorSchedule.getBreakFrom(), doctorSchedule.getBreakTo()) && (!isRequestedDateIsToday || currentSlotStart.toLocalTime().isAfter(currentTime))) {

                availableSlots.add(new TimeSlot(currentSlotStart, currentSlotEnd));
            }

            currentSlotStart = currentSlotEnd;
        }

        return availableSlots;
    }

    @Override
    public void deleteAppointment(Long appointmentId, Long patientId, String roles) {
        checkForPatientPermission(roles);
        var appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new NotFoundException("No appointment with id: " + appointmentId));
        if (appointment.getPatientId().equals(patientId)) {
            appointmentRepository.delete(appointment);
        }
    }

    @Override
    public AppointmentResponse updateAppointment(UpdateAppointmentRequest updateAppointmentRequest, Long patientId, String roles) {
        checkForPatientPermission(roles);
        var appointment = appointmentRepository.findById(updateAppointmentRequest.appointmentId())
                .orElseThrow(() -> new NotFoundException(
                        "No appointment with id: " + updateAppointmentRequest.appointmentId())
                );

        if (!appointment.getPatientId().equals(patientId)) {
            throw new UnauthorizedException();
        }

        var newAppointmentTime = updateAppointmentRequest.newAppointmentTime();
        var endOfAppointment = newAppointmentTime.plusMinutes(30);
        var appointments = appointmentRepository.
                findAppointmentsByDoctorIdAndAppointmentDateTimeGreaterThanEqualAndAppointmentDateTimeLessThanEqual
                        (appointment.getDoctorId(), newAppointmentTime, endOfAppointment);

        if (!isDoctorAvailable(updateAppointmentRequest.newAppointmentTime(), appointment.getDoctorId(), appointments)) {
            throw new DoctorIsNotAvailableAtThisTimeSlotException(appointment.getDoctorId(), newAppointmentTime);
        }


        appointment.setAppointmentDateTime(newAppointmentTime);
        appointment.setEndOFAppointmentDateTime(endOfAppointment);

        appointmentRepository.save(appointment);

        kafkaService.sendAppointmentNotification(appointment);

        return new AppointmentResponse(newAppointmentTime, endOfAppointment);
    }

    private boolean isDoctorAvailable(LocalDateTime requestedTime, Long doctorId, List<Appointment> appointments) {
        if (!appointments.isEmpty()) {
            return false;
        }
        var schedule = doctorScheduleRepository.findByDoctorId(doctorId).orElseThrow(() -> new NotFoundException("No schedule for doctor with id: " + doctorId));

        var requestedDayOfTheWeek = requestedTime.getDayOfWeek();
        var requestedLocalTime = requestedTime.toLocalTime();

        boolean isDuringWorkingDay = schedule.getWorkingDays().contains(requestedDayOfTheWeek);
        boolean isDuringWorkingHours = requestedLocalTime.isAfter(schedule.getStartTime()) && requestedLocalTime.isBefore(schedule.getEndTime());
        boolean isDuringLunchBreak = requestedLocalTime.isAfter(schedule.getBreakFrom()) && requestedLocalTime.isBefore(schedule.getBreakTo());

        return isDuringWorkingDay && isDuringWorkingHours && !isDuringLunchBreak;
    }


    /**
     * Checks if a time slot is within the range of a specified time period.
     *
     * @param currentSlotStart The start time of the current time slot.
     * @param currentSlotEnd   The end time of the current time slot.
     * @param breakStart       The start time of the  break.
     * @param breakEnd         The end time of the  break.
     * @return True if the time slot is within the specified time period, false otherwise.
     */
    private boolean isTimeBetween(LocalTime currentSlotStart, LocalTime currentSlotEnd, LocalTime breakStart, LocalTime breakEnd) {
        return (currentSlotStart.isAfter(breakStart) || currentSlotStart.equals(breakStart)) && currentSlotStart.isBefore(breakEnd) || currentSlotEnd.isAfter(breakStart) && (currentSlotEnd.isBefore(breakEnd) || currentSlotEnd.equals(breakEnd));
    }

    /**
     * Checks if a given date is a working day based on the doctor's schedule and holidays.
     *
     * @param doctorSchedule The doctor's schedule containing working days and break times.
     * @param vacations      The list of holidays.
     * @param requestedDate  The date to be checked.
     * @return True if the requested date is a working day, considering the doctor's schedule and holidays; false otherwise.
     */
    private boolean isValidWorkingDayForDoctor(DoctorSchedule doctorSchedule, List<Vacation> vacations, LocalDate requestedDate) {

        if (isNationalHoliday(requestedDate)) {
            return false;
        }

        List<LocalDate> holidayDates = vacations.stream().map(Vacation::getVacationLocalDate).toList();

        var isOnHoliday = holidayDates.contains(requestedDate);
        if (isOnHoliday) {
            return false;
        }
        return doctorSchedule.getWorkingDays().contains(requestedDate.getDayOfWeek());
    }

    private boolean isNationalHoliday(LocalDate date) {
        return nationalHolidaysInGermany.contains(date);
    }
}
