package emil.dobrev.services.service;

import emil.dobrev.services.dto.AppointmentResponse;
import emil.dobrev.services.dto.CreateAppointmentRequest;
import emil.dobrev.services.exception.DoctorIsNotAvailableAtThisTimeSlotException;
import emil.dobrev.services.exception.NotFoundException;
import emil.dobrev.services.model.Appointment;
import emil.dobrev.services.repository.AppointmentRepository;
import emil.dobrev.services.repository.DoctorScheduleRepository;
import emil.dobrev.services.service.interfaces.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static emil.dobrev.services.shared.PermissionsUtils.checkForPatientOrDoctorPermission;
import static emil.dobrev.services.shared.PermissionsUtils.checkForPatientPermission;


@Service
@RequiredArgsConstructor
public class AppointmentServiceImp implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;

    @Override
    public AppointmentResponse create(CreateAppointmentRequest createAppointmentRequest, Long patientId, String roles) {
        checkForPatientPermission(roles);
        var requestedDateTime = createAppointmentRequest.appointmentDateTime();
        var endOfAppointment = requestedDateTime.plusMinutes(30);
        var doctorId = createAppointmentRequest.doctorId();

        var appointments = appointmentRepository
                .findAppointmentsByDoctorIdAndAppointmentDateTimeGreaterThanEqualAndAppointmentDateTimeLessThanEqual(
                        doctorId,
                        requestedDateTime,
                        endOfAppointment
                );
        if (!isDoctorAvailable(
                createAppointmentRequest.appointmentDateTime(),
                createAppointmentRequest.doctorId(),
                appointments
        )
        ) {
            throw new DoctorIsNotAvailableAtThisTimeSlotException(
                    String.format("Doctor with ID %d is not available at %s.",
                            doctorId, requestedDateTime));
        }

        var appointment = Appointment.builder()
                .appointmentDateTime(requestedDateTime)
                .endOFAppointmentDateTime(endOfAppointment)
                .doctorId(doctorId)
                .patientId(patientId)
                .build();

        appointment = appointmentRepository.save(appointment);
        return new AppointmentResponse(
                appointment.getAppointmentDateTime(),
                appointment.getEndOFAppointmentDateTime());
    }

    @Override
    public void getAllAvailableSlots(Long doctorId, String roles, LocalDate requestedDate) {
        checkForPatientOrDoctorPermission(roles);
        var doctorSchedule = doctorScheduleRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new NotFoundException("No schedule for doctor with doctorId:" + doctorId));

        List<Appointment> existingAppointments = appointmentRepository.findAppointmentsByDoctorIdAndAppointmentDateTimeBetween(
                doctorId,
                requestedDate.atStartOfDay(),
                requestedDate.atTime(LocalTime.MAX)
        );

    }

    private boolean isDoctorAvailable(LocalDateTime requestedTime, Long doctorId, List<Appointment> appointments) {
        var schedule = doctorScheduleRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new NotFoundException("No schedule for doctor with id: " + doctorId));

        var requestedDayOfTheWeek = requestedTime.getDayOfWeek();
        var requestedLocalTime = requestedTime.toLocalTime();

        boolean isDuringWorkingDay = schedule.getWorkingDays().contains(requestedDayOfTheWeek);
        boolean isDuringWorkingHours = requestedLocalTime.isAfter(schedule.getStartTime())
                && requestedLocalTime.isBefore(schedule.getEndTime());
        boolean isDuringLunchBreak = requestedLocalTime.isAfter(schedule.getBreakFrom())
                && requestedLocalTime.isBefore(schedule.getBreakTo());

        return isDuringWorkingDay && isDuringWorkingHours && !isDuringLunchBreak && appointments.isEmpty();
    }
}
