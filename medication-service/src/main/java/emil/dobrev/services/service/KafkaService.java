package emil.dobrev.services.service;

import emil.dobrev.services.dto.MedicationNotification;
import emil.dobrev.services.model.MedicationSchedule;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static emil.dobrev.services.constant.Constants.MEDICATION;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaService {

    private final KafkaTemplate<String, MedicationNotification> kafkaTemplate;

    @Async
    public void sendMedicationNotification(@NonNull MedicationSchedule medicationSchedule) {
        MedicationNotification medicationNotification = setMedicationNotification(medicationSchedule);

        kafkaTemplate.send(MEDICATION, medicationNotification);
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
}
