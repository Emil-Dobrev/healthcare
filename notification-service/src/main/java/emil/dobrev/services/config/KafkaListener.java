package emil.dobrev.services.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import emil.dobrev.services.model.AppointmentNotification;
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

    @org.springframework.kafka.annotation.KafkaListener(
            topics = "appointment",
            groupId = "appointment-group"
    )
    void listener(String data) {
        try {
            AppointmentNotification appointment = objectMapper.readValue(data, AppointmentNotification.class);
            log.info("Sending email for appointment {}", appointment.appointmentId());
            var emailMetaInformation = emailService.buildEmailMetaInformation(appointment);
            emailService.sendEmail(emailMetaInformation);
        } catch (JsonProcessingException e) {
            log.error("Error processing appointment notification: " + e.getMessage(), e);
        }
    }
}
