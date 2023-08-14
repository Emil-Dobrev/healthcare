package emil.dobrev.services.config;

import emil.dobrev.services.exception.NotFoundException;
import emil.dobrev.services.model.Notification;
import emil.dobrev.services.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailListener {

    private final NotificationRepository notificationRepository;


    @EventListener
    public void emailFailed(EmailEvent emailEvent) {
        log.info("Failed to send email");
        var notificationId = emailEvent.getNotification().getId();
        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() -> new NotFoundException("No notification with id: " + notificationId));
        notification.setSend(false);
        notificationRepository.save(notification);
    }
}
