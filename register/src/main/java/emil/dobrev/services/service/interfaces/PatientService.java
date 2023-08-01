package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.PatientDTO;

public interface PatientService {
    PatientDTO getPatientById(Long id);
}
