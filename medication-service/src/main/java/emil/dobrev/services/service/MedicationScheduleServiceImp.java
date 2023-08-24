package emil.dobrev.services.service;

import emil.dobrev.services.dto.CreateMedicationScheduleRequest;
import emil.dobrev.services.dto.MedicationScheduleDTO;
import emil.dobrev.services.exception.NotFoundException;
import emil.dobrev.services.exception.UnauthorizedException;
import emil.dobrev.services.model.MedicationSchedule;
import emil.dobrev.services.repository.MedicationScheduleRepository;
import emil.dobrev.services.service.interfaces.MedicationScheduleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static emil.dobrev.services.shared.PermissionsUtils.checkForUserPermissions;

@Service
@RequiredArgsConstructor
public class MedicationScheduleServiceImp implements MedicationScheduleService {

    private final MedicationScheduleRepository medicationScheduleRepository;
    private final KafkaService kafkaService;
    private final ModelMapper modelMapper;

    @Override
    public void createMedicationSchedule(Long userId, String roles, CreateMedicationScheduleRequest request) {
        checkForUserPermissions(roles);

        LocalDateTime timeFirstDosageTaken = request.getTimeFirstDosageTaken();

        MedicationSchedule medicationSchedule = MedicationSchedule.builder()
                .userId(userId)
                .dosageUnit(request.getDosageUnit())
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .dosage(request.getDosage())
                .durationInHoursBetweenDoses(request.getDurationBetweenDoses())
                .frequencyPerDay(request.getFrequency())
                .build();

        if (timeFirstDosageTaken != null) {
            medicationSchedule.setFirstDosage(timeFirstDosageTaken);
            medicationSchedule.setActive(true);
        }

        medicationScheduleRepository.save(medicationSchedule);

    }

    @Override
    public List<MedicationScheduleDTO> getAllMedicationScheduleForUser(Long userId, String roles) {
        checkForUserPermissions(roles);

        var medicationSchedules = medicationScheduleRepository.
                findAllByUserId(userId)
                .orElseThrow(() -> new NotFoundException("No medication schedules for user with id: " + userId));
        return medicationSchedules.stream()
                .map(medicationSchedule -> modelMapper.map(medicationSchedule, MedicationScheduleDTO.class))
                .toList();
    }

    @Override
    public void setTimeFirstDosageTaken(Long userId, String roles, Long medicationScheduleId, LocalDateTime timeFirstDosageTaken) {
        checkForUserPermissions(roles);

        var medicationSchedules = medicationScheduleRepository.findById(medicationScheduleId)
                .orElseThrow(() -> new NotFoundException("No medication schedules  with id: " + medicationScheduleId));

        if (!medicationSchedules.getUserId().equals(userId)) {
            throw new UnauthorizedException("Unauthorized");
        }
        medicationSchedules.setFirstDosage(timeFirstDosageTaken);
        medicationSchedules.setActive(true);
        medicationScheduleRepository.save(medicationSchedules);
    }
}
