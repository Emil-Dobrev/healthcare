package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.MedicationScheduleDTO;

public interface MedicationScheduleService {
    void createMedicationSchedule(Long userId, String roles, MedicationScheduleDTO medicationScheduleDTO);
}
