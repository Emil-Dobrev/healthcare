package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.PatientDTO;

import java.util.List;

public interface PatientService {
    PatientDTO getPatientById(Long id);

    List<PatientDTO> getAllPatients();
}
