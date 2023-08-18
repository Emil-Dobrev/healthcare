package emil.dobrev.services.config;

import emil.dobrev.services.exception.NotFoundException;
import emil.dobrev.services.model.Notification;
import emil.dobrev.services.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
public class EmailListener {

    private final NotificationRepository notificationRepository;


    @EventListener
    public void emailFailed(EmailEvent emailEvent) {
        log.info("Failed to send email");
        if(emailEvent.getValue() instanceof Notification notification) {
            var notificationId = notification.getId();
            Notification not = notificationRepository
                    .findById(notificationId)
                    .orElseThrow(() -> new NotFoundException("No notification with id: " + notificationId));
            not.setEmailSend(false);
            notificationRepository.save(not);
        }

    }
}
