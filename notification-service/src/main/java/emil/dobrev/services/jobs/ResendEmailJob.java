package emil.dobrev.services.jobs;

import emil.dobrev.services.repository.NotificationRepository;
import emil.dobrev.services.service.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ResendEmailJob {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 */15 * ? * *")
    void resendEmail() {
        log.info("Job for resending failed appointment emails started");
        notificationRepository.findByIsEmailSendFalse()
                .forEach(notification -> {
                    emailService.sendEmail(notification.getEmailMetaInformation(), notification);
                    notification.setEmailSend(true);
                    notificationRepository.save(notification);
                });
    }
}
