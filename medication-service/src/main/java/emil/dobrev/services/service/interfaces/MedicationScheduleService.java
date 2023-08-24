package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.CreateMedicationScheduleRequest;
import emil.dobrev.services.dto.MedicationScheduleDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicationScheduleService {
    void createMedicationSchedule(Long userId, String roles, CreateMedicationScheduleRequest request);

    List<MedicationScheduleDTO> getAllMedicationScheduleForUser(Long userId, String roles);

    void setTimeFirstDosageTaken(Long userId, String roles, Long medicationScheduleId, LocalDateTime timeFirstDosageTaken);
}
