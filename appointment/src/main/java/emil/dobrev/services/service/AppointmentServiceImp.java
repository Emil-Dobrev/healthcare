package emil.dobrev.services.service;

import emil.dobrev.services.dto.CreateAppointmentRequest;
import emil.dobrev.services.exception.DoctorIsNotAvailableAtThisTimeSlotException;
import emil.dobrev.services.exception.NotFoundException;
import emil.dobrev.services.model.Appointment;
import emil.dobrev.services.repository.AppointmentRepository;
import emil.dobrev.services.repository.DoctorScheduleRepository;
import emil.dobrev.services.service.interfaces.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AppointmentServiceImp  implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;

    @Override
    public void create(CreateAppointmentRequest createAppointmentRequest) {
        if(!isDoctorAvailable(createAppointmentRequest.appointmentDateTime(), createAppointmentRequest.doctorId())) {
            throw new DoctorIsNotAvailableAtThisTimeSlotException("Doctor with id: " + createAppointmentRequest.doctorId()
            + " is not available at:" + createAppointmentRequest.appointmentDateTime());
        }

        var appointment = Appointment.builder()
                .appointmentDateTime(createAppointmentRequest.appointmentDateTime())
                .doctorId(createAppointmentRequest.doctorId())
                .patientId(createAppointmentRequest.patientId())
                .build();

        appointmentRepository.save(appointment);


        //TODO check if appointmentDate is free for this doctor
    }

    private boolean isDoctorAvailable(LocalDateTime requestedTime, Long doctorId) {
        var schedule = doctorScheduleRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new NotFoundException("No schedule for doctor with id: " + doctorId));

        var requestedDayOfTheWeek = requestedTime.getDayOfWeek();
        var requestedLocalTime = requestedTime.toLocalTime();

        boolean isDuringWorkingDay = schedule.getWorkingDays().contains(requestedDayOfTheWeek);
        boolean isDuringWorkingHours = requestedLocalTime.isAfter(schedule.getStartTime())
                && requestedLocalTime.isBefore(schedule.getEndTime());
        boolean isDuringLunchBreak = requestedLocalTime.isAfter(schedule.getBreakFrom())
                && requestedLocalTime.isBefore(schedule.getBreakTo());

        return isDuringWorkingDay && isDuringWorkingHours && isDuringLunchBreak;
    }
}
