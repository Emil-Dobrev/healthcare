package emil.dobrev.services.jobs;

import emil.dobrev.services.model.Notification;
import emil.dobrev.services.repository.NotificationRepository;
import emil.dobrev.services.service.interfaces.EmailMetaInformation;
import emil.dobrev.services.service.interfaces.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemainderNotificationJobTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private RemainderNotificationJob remainderNotificationJob;

    @Test
    void testSendRemainderForAppointmentJob() {
        var today = LocalDate.now();
        LocalDateTime startOfDay = today.atTime(LocalTime.MIN);
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        List<Notification> mockAppointments = new ArrayList<>();

        EmailMetaInformation emailMetaInformation = new EmailMetaInformation(
                "Test test",
                "Text",
                "Title",
                "Subject",
                "Email",
                "header",
                startOfDay.plusMinutes(200)
        );

        Notification notification = Notification.builder()
                .id("test")
                .emailMetaInformation(emailMetaInformation)
                .isEmailSend(true)
                .build();

        mockAppointments.add(notification);
        mockAppointments.add(notification);

        when(notificationRepository.findAllByEmailMetaInformationTimeOfAppointmentBetween(startOfDay, endOfDay))
                .thenReturn(mockAppointments);

        remainderNotificationJob.sendRemainderForAppointmentJob();

        // Verify that the emailService.sendEmail() method was called for each appointment
        verify(emailService, times(mockAppointments.size())).sendEmail(any(EmailMetaInformation.class), any(Notification.class));
    }
}