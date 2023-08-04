package emil.dobrev.services.service;

import emil.dobrev.services.dto.DoctorDTO;
import emil.dobrev.services.dto.UpdateDoctorRequest;
import emil.dobrev.services.exception.NoSuchElementException;
import emil.dobrev.services.repository.DoctorRepository;
import emil.dobrev.services.service.interfaces.DoctorService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DoctorServiceImp implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public DoctorDTO getDoctorById(Long id) {
        var doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No doctor with id: " + id));

        return modelMapper.map(doctor, DoctorDTO.class);
    }

    @Override
    public List<DoctorDTO> getAllDoctors() {
        var doctors = doctorRepository.findAll();
        return doctors
                .stream()
                .map(doctor -> modelMapper.map(doctor, DoctorDTO.class))
                .toList();
    }

    @Override
    public DoctorDTO updateDoctor(String email, UpdateDoctorRequest updateDoctorRequest) {
        var exiStingDoctor = doctorRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));
        if (!exiStingDoctor.getFirstName().equals(updateDoctorRequest.firstName())) {
            exiStingDoctor.setFirstName(updateDoctorRequest.firstName());
        }
        if (!exiStingDoctor.getLastName().equals(updateDoctorRequest.lastName())) {
            exiStingDoctor.setLastName(updateDoctorRequest.lastName());
        }
        if (updateDoctorRequest.password() != null && !passwordEncoder.encode(updateDoctorRequest.password()).matches(exiStingDoctor.getPassword())) {
            exiStingDoctor.setPassword(passwordEncoder.encode(updateDoctorRequest.password()));
        }
        if (!exiStingDoctor.getBirthdate().equals(updateDoctorRequest.birthdate())) {
            exiStingDoctor.setBirthdate(updateDoctorRequest.birthdate());
        }
        doctorRepository.save(exiStingDoctor);
        return modelMapper.map(exiStingDoctor, DoctorDTO.class);
    }
}
