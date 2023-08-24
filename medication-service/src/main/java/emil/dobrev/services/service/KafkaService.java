package emil.dobrev.services.service;

import emil.dobrev.services.dto.MedicationNotification;
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
    public void sendMedicationNotification(@NonNull MedicationNotification medicationNotification) {
        System.out.println(medicationNotification);
        log.error("test");
        kafkaTemplate.send(MEDICATION, medicationNotification);
    }
}
