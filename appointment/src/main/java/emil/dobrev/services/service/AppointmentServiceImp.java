package emil.dobrev.services.service;

import emil.dobrev.services.dto.CreateAppointmentRequest;
import emil.dobrev.services.model.Appointment;
import emil.dobrev.services.repository.AppointmentRepository;
import emil.dobrev.services.service.interfaces.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AppointmentServiceImp  implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Override
    public void create(CreateAppointmentRequest createAppointmentRequest) {
        Appointment appointment = Appointment.builder()
                .appointmentDateTime(createAppointmentRequest.appointmentDateTime())
                .doctorId(createAppointmentRequest.doctorId())
                .patientId(createAppointmentRequest.patientId())
                .build();

        appointmentRepository.save(appointment);


        //TODO check if appointmentDate is free for this doctor
    }
}
