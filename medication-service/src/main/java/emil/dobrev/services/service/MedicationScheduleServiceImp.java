package emil.dobrev.services.service;

import emil.dobrev.services.dto.MedicationScheduleDTO;
import emil.dobrev.services.model.MedicationSchedule;
import emil.dobrev.services.repository.MedicationScheduleRepository;
import emil.dobrev.services.service.interfaces.MedicationScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static emil.dobrev.services.shared.PermissionsUtils.checkForUserPermissions;

@Service
@RequiredArgsConstructor
public class MedicationScheduleServiceImp implements MedicationScheduleService {

    private final MedicationScheduleRepository medicationScheduleRepository;
    private final KafkaService kafkaService;

    @Override
    public void createMedicationSchedule(Long userId, String roles, MedicationScheduleDTO medicationScheduleDTO) {
        checkForUserPermissions(roles);

        MedicationSchedule medicationSchedule = MedicationSchedule.builder()
                .userId(medicationScheduleDTO.getUserId())
                .dosageUnit(medicationScheduleDTO.getDosageUnit())
                .name(medicationScheduleDTO.getName())
                .startDate(medicationScheduleDTO.getStartDate())
                .endDate(medicationScheduleDTO.getEndDate())
                .daysOfWeek(medicationScheduleDTO.getDaysOfWeek())
                .dosage(medicationScheduleDTO.getDosage())
                .durationBetweenDoses(medicationScheduleDTO.getDurationBetweenDoses())
                .frequency(medicationScheduleDTO.getFrequency())
                .build();

        medicationScheduleRepository.save(medicationSchedule);
        kafkaService.sendMedicationNotification(medicationScheduleDTO);
    }
}
