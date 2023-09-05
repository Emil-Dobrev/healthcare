package emil.dobrev.services.job;

import emil.dobrev.services.model.MedicationSchedule;
import emil.dobrev.services.repository.MedicationScheduleRepository;
import emil.dobrev.services.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendRemainderForNextDosageJob {

    private final MedicationScheduleRepository medicationScheduleRepository;
    private final KafkaService kafkaService;

    @Scheduled(cron = "0 */10 * * * *")
// runs every 10 minutes
    void sendRemindersForNextDosage() {
        log.info("Job for sending remainders for next dosage started");
        var medicationSchedules = medicationScheduleRepository.findAllByIsActive(true);
        var today = LocalDate.now();

        medicationSchedules
                .forEach(medicationSchedule -> {
                    if (!isDosagePerDayIsReached(medicationSchedule) &&
                            !hasNotificationBeenSentForCurrentDosage(medicationSchedule.getTimeForLastDosageNotificationSend())) {
                        var nextDosage = calculateTimeForNextDosage(medicationSchedule);
                        medicationSchedule.setTimeForNextDosage(nextDosage);
                        medicationSchedule.setTimeForLastDosageNotificationSend(nextDosage);

                        int dosageTakenToday = medicationSchedule.getDosageTakenToday();
                        if (isNextDosageToday(medicationSchedule, today)) {
                            medicationSchedule.setDosageTakenToday(dosageTakenToday + 1);
                        } else {
                            medicationSchedule.setDosageTakenToday(1);
                        }

                        medicationScheduleRepository.save(medicationSchedule);

                        kafkaService.sendMedicationNotification(medicationSchedule);
                    }
                });
    }


    private LocalDateTime calculateTimeForNextDosage(MedicationSchedule medicationSchedule) {
        if (medicationSchedule.getTimeForNextDosage() == null) {
            return medicationSchedule.getFirstDosage()
                    .plusMinutes(medicationSchedule.getDurationInHoursBetweenDoses());
        } else {
            return medicationSchedule.getTimeForNextDosage()
                    .plusHours(medicationSchedule.getDurationInHoursBetweenDoses());

        }
    }

    private boolean isDosagePerDayIsReached(MedicationSchedule medicationSchedule) {
        return medicationSchedule.getDosageTakenToday() >= medicationSchedule.getFrequencyPerDay();
    }

    private boolean isNextDosageToday(MedicationSchedule medicationSchedule, LocalDate today) {
        Optional<LocalDateTime> nextDosage = Optional.ofNullable(medicationSchedule.getTimeForNextDosage());
        return nextDosage.map(dosage -> dosage.toLocalDate().isEqual(today)).orElse(true);
    }

    private boolean hasNotificationBeenSentForCurrentDosage(LocalDateTime timeForLastDosageNotificationSend) {
        var time = Optional.ofNullable(timeForLastDosageNotificationSend);
        var now = LocalDateTime.now();
        return time
                .map(t -> t.isAfter(now)).orElse(false);
    }
}
