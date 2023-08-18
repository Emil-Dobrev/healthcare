package emil.dobrev.services.service;

import emil.dobrev.services.config.EmailEvent;
import emil.dobrev.services.model.AppointmentNotification;
import emil.dobrev.services.model.Notification;
import emil.dobrev.services.model.User;
import emil.dobrev.services.repository.NotificationRepository;
import emil.dobrev.services.service.interfaces.EmailMetaInformation;
import emil.dobrev.services.service.interfaces.EmailService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImp implements EmailService {

    private final JavaMailSender javaMailSender;
    private final RestTemplate restTemplate;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final NotificationRepository notificationRepository;
    private final String SENDER_EMAIL = "dobrev93sl@gmail.com";
    public static final String TEXT_PLACEHOLDER = "TEXT_PLACEHOLDER";
    public static final String PLACEHOLDER_TITLE = "PLACEHOLDER_TITLE";
    public static final String PLACEHOLDER_HEADER = "PLACEHOLDER_HEADER";
    private static final String STATIC_EMAIL_TEMPLATE_HTML = "static/EmailTemplate.html";
    private static final String API_V_1_PATIENTS = "http://localhost:8080/api/v1/patients/";
    private static final String API_V_1_DOCTORS = "http://localhost:8080/api/v1/doctors/";


    @Override
    public <T> void sendEmail(EmailMetaInformation emailMetaInformation, T object) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessage.setFrom(new InternetAddress(SENDER_EMAIL));
            helper.setText(buildEmail(emailMetaInformation), true);
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailMetaInformation.email()));
            helper.setSubject(emailMetaInformation.subject());
            javaMailSender.send(mimeMessage);
        } catch (MessagingException | RuntimeException | IOException e) {
            log.error("Failed to send email: {}" + e.getMessage());
            applicationEventPublisher.publishEvent(new EmailEvent<>(this, object));
        }
    }

    public EmailMetaInformation buildEmailMetaInformation(AppointmentNotification appointmentNotification) {

        var patient = restTemplate
                .getForEntity(API_V_1_PATIENTS + appointmentNotification.patientId(), User.class)
                .getBody();
        var doctor = restTemplate
                .getForEntity(API_V_1_DOCTORS + appointmentNotification.doctorId(), User.class)
                .getBody();

        var doctorFullName = String.format("%s %s", doctor.getFirstName(), doctor.getLastName());
        var patientFullName = String.format("%s %s", patient.getFirstName(), patient.getLastName());
        var text = String.format("You have an appointment at %s, with doctor: %s", appointmentNotification.appointmentDateTime(),
                doctorFullName);
        var title = "Appointment at: " + appointmentNotification.appointmentDateTime();
        var subject = "You have appointment at:" + appointmentNotification.appointmentDateTime();


        var emailMetaInformation =  new EmailMetaInformation(
                patientFullName,
                text,
                title,
                subject,
                patient.getEmail(),
                "",
                appointmentNotification.appointmentDateTime()
        );

        return emailMetaInformation;
    }

    private String buildEmail(EmailMetaInformation emailMetaInformation) throws IOException {
        return StreamUtils.copyToString(new ClassPathResource(STATIC_EMAIL_TEMPLATE_HTML).getInputStream(), StandardCharsets.UTF_8)
                .replace(PLACEHOLDER_TITLE, emailMetaInformation.title())
                .replace(PLACEHOLDER_HEADER, emailMetaInformation.header())
                .replace(TEXT_PLACEHOLDER, emailMetaInformation.text());
    }
}
