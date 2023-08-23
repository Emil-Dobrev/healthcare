package emil.dobrev.services.job;

import emil.dobrev.services.dto.MedicationNotification;
import emil.dobrev.services.model.MedicationSchedule;
import emil.dobrev.services.repository.MedicationScheduleRepository;
import emil.dobrev.services.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    void SendRemainderForNextDosageJob() {
        log.info("Job for sending remainders for next dosage started");
        var medicationSchedules = medicationScheduleRepository.findAllByIsActive(true);
        var today = LocalDate.now();

        medicationSchedules
                .forEach(medicationSchedule -> {
                    if (!isDosagePerDayIsReached(medicationSchedule, today)) {
                        var nextDosage = calculateTimeForNextDosage(medicationSchedule);
                        if (!nextDosage.equals(medicationSchedule.getTimeForNextDosage())) {
                            medicationSchedule.setTimeForNextDosage(nextDosage);

                            int dosageTakenToday = medicationSchedule.getDosageTakenToday();
                            if (isNextDosageToday(medicationSchedule, today)) {
                                medicationSchedule.setDosageTakenToday(dosageTakenToday + 1);
                            } else {
                                medicationSchedule.setDosageTakenToday(1);
                            }

                            medicationScheduleRepository.save(medicationSchedule);

                            var medicationNotification = setMedicationNotification(medicationSchedule);
                            kafkaService.sendMedicationNotification(medicationNotification);
                        }
                    }
                });
    }

    private MedicationNotification setMedicationNotification(MedicationSchedule medicationSchedule) {
        return new MedicationNotification(
                medicationSchedule.getUserId(),
                medicationSchedule.getName(),
                medicationSchedule.getDosage(),
                medicationSchedule.getDosageUnit(),
                medicationSchedule.getFrequencyPerDay(),
                medicationSchedule.getStartDate(),
                medicationSchedule.getEndDate(),
                medicationSchedule.getDurationInHoursBetweenDoses(),
                medicationSchedule.getFirstDosage(),
                medicationSchedule.getTimeForNextDosage()
        );
    }

    private LocalDateTime calculateTimeForNextDosage(MedicationSchedule medicationSchedule) {
        if (medicationSchedule.getTimeForNextDosage() == null) {
            return medicationSchedule.getFirstDosage()
                    .plusMinutes(medicationSchedule.getDurationInHoursBetweenDoses());
        } else {
            return medicationSchedule.getTimeForNextDosage()
                    .plusMinutes(medicationSchedule.getDurationInHoursBetweenDoses());

        }
    }

    private boolean isDosagePerDayIsReached(MedicationSchedule medicationSchedule, LocalDate today) {
        return medicationSchedule.getDosageTakenToday() >= medicationSchedule.getFrequencyPerDay();
    }

    private boolean isNextDosageToday(MedicationSchedule medicationSchedule, LocalDate today) {
        var isNextDosageToday = true;
        var nextDosage = Optional.ofNullable(medicationSchedule.getTimeForNextDosage());
        if (nextDosage.isPresent()) {
            nextDosage.ifPresent(LocalDateTime::toLocalDate);
            isNextDosageToday = today.equals(nextDosage);
        }
        return isNextDosageToday;
    }
}
