package emil.dobrev.services.service;

import emil.dobrev.services.dto.AppointmentNotification;
import emil.dobrev.services.model.Appointment;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static emil.dobrev.services.constant.Constants.APPOINTMENT;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, AppointmentNotification> kafkaTemplate;

    @Async
    public void sendAppointmentNotification(@NonNull Appointment appointment) {
        var appointmentNotification = new AppointmentNotification(
                appointment.getPatientId(),
                appointment.getDoctorId(),
                appointment.getAppointmentDateTime(),
                appointment.getEndOFAppointmentDateTime());
        kafkaTemplate.send(APPOINTMENT, appointmentNotification);
    }
}

