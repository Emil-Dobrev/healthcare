package emil.dobrev.services.service;

import emil.dobrev.services.dto.PatientDTO;
import emil.dobrev.services.exception.NoSuchElementException;
import emil.dobrev.services.repository.PatientRepository;
import emil.dobrev.services.service.interfaces.PatientService;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImp implements PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Override
    public PatientDTO getPatientById(Long id) {
        var patient = patientRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No patient with id: " + id));
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
