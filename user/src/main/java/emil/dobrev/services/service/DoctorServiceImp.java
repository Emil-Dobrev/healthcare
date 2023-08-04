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
    public DoctorDTO updateDoctor(Long id, String role, UpdateDoctorRequest updateDoctorRequest) {
        var existingUser = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));
        if (!existingUser.getFirstName().equals(updateDoctorRequest.firstName())) {
            existingUser.setFirstName(updateDoctorRequest.firstName());
        }
        if (!existingUser.getLastName().equals(updateDoctorRequest.lastName())) {
            existingUser.setLastName(updateDoctorRequest.lastName());
        }
        if (updateDoctorRequest.password() != null && !passwordEncoder.encode(updateDoctorRequest.password()).matches(existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(updateDoctorRequest.password()));
        }
        if (!existingUser.getBirthdate().equals(updateDoctorRequest.birthdate())) {
            existingUser.setBirthdate(updateDoctorRequest.birthdate());
        }
        doctorRepository.save(existingUser);
        return new DoctorDTO();
    }
}
