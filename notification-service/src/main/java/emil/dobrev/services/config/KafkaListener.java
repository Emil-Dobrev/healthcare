package emil.dobrev.services.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import emil.dobrev.services.model.AppointmentNotification;
import emil.dobrev.services.model.MedicationNotification;
import emil.dobrev.services.model.Notification;
import emil.dobrev.services.repository.NotificationRepository;
import emil.dobrev.services.service.interfaces.EmailMetaInformation;
import emil.dobrev.services.service.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaListener {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final EmailService emailService;
    private final NotificationRepository notificationRepository;

    @org.springframework.kafka.annotation.KafkaListener(
            topics = "appointment",
            groupId = "appointment-group"
    )
    void listener(String data) {
        try {
            AppointmentNotification appointment = objectMapper.readValue(data, AppointmentNotification.class);
            log.info("Sending email for appointment {}", appointment.appointmentId());
            EmailMetaInformation emailMetaInformation = emailService.buildEmailMetaInformation(appointment);
            var notification = notificationRepository.save(new Notification(emailMetaInformation, appointment.appointmentDateTime()));
            emailService.sendEmail(emailMetaInformation, notification);
        } catch (JsonProcessingException e) {
            log.error("Error processing appointment notification: " + e.getMessage(), e);
        }
    }

    @org.springframework.kafka.annotation.KafkaListener(
            topics = "medication",
            groupId = "medication-group"
    )
    void medicationListener(String data) {
        try {
            MedicationNotification medicationNotification = objectMapper.readValue(data, MedicationNotification.class);
            log.info("Sending email for medication remainder for user with id: {}", medicationNotification.userId());
            var emailMetaInformation = emailService.buildEmailMetaInformation(medicationNotification);
            emailService.sendEmail(emailMetaInformation, medicationNotification);
        } catch (JsonProcessingException e) {
            log.error("Error processing appointment notification: " + e.getMessage(), e);
        }
    }
}
