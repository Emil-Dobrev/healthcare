package emil.dobrev.services.service;

import emil.dobrev.services.dto.PatientDTO;
import emil.dobrev.services.repository.PatientRepository;
import emil.dobrev.services.service.interfaces.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static emil.dobrev.services.utils.JpaRepositoryUtils.getOrThrow;

@Service
@RequiredArgsConstructor
public class PatientServiceImp implements PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Override
    public PatientDTO getPatientById(Long id) {
        var patient = getOrThrow(patientRepository, id);
        return modelMapper.map(patient, PatientDTO.class);
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        return patientRepository
                .findAll()
                .stream()
                .map(patient -> modelMapper.map(patient, PatientDTO.class))
                .toList();
    }
}
