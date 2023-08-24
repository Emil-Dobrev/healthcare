package emil.dobrev.services.jobs;

import emil.dobrev.services.repository.NotificationRepository;
import emil.dobrev.services.service.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class RemainderNotificationJob {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 1 * * * *") // 1am every day
    void sendRemainderForAppointmentJob() {
        log.info("Starting job for sending reminders about appointments");
        var today = LocalDate.now();
        LocalDateTime startOfDay = today.atTime(LocalTime.MIN);
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        var appointments = notificationRepository.findAllByTimeOfAppointmentBetween(startOfDay, endOfDay);
        appointments
                .forEach(appointment -> emailService.sendEmail(appointment.getEmailMetaInformation(), appointment));
    }
}
