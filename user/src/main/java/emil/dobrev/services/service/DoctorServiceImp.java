package emil.dobrev.services.service;

import emil.dobrev.services.dto.DoctorDTO;
import emil.dobrev.services.dto.UpdateDoctorRequest;
import emil.dobrev.services.enums.DoctorSpecialization;
import emil.dobrev.services.exception.NoSuchElementException;
import emil.dobrev.services.exception.NotFoundException;
import emil.dobrev.services.repository.DoctorRepository;
import emil.dobrev.services.service.interfaces.DoctorService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public List<DoctorDTO> getAllDoctors(DoctorSpecialization specialization) {
        var spec = specialization != null ? specialization.toString() : null;
        var doctors = doctorRepository.findAll(spec);
        return doctors
                .stream()
                .map(doctor -> modelMapper.map(doctor, DoctorDTO.class))
                .toList();
    }

    @Override
    public DoctorDTO updateDoctor(Long id, UpdateDoctorRequest updateDoctorRequest) {
        var existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!existingDoctor.getFirstName().equals(updateDoctorRequest.firstName())) {
            existingDoctor.setFirstName(updateDoctorRequest.firstName());
        }
        if (!existingDoctor.getLastName().equals(updateDoctorRequest.lastName())) {
            existingDoctor.setLastName(updateDoctorRequest.lastName());
        }
        if (updateDoctorRequest.password() != null && !passwordEncoder.encode(updateDoctorRequest.password()).matches(existingDoctor.getPassword())) {
            existingDoctor.setPassword(passwordEncoder.encode(updateDoctorRequest.password()));
        }
        if (!existingDoctor.getBirthdate().equals(updateDoctorRequest.birthdate())) {
            existingDoctor.setBirthdate(updateDoctorRequest.birthdate());
        }
        if (!existingDoctor.getSpecialization().equals(updateDoctorRequest.doctorSpecialization())) {
            existingDoctor.setSpecialization(updateDoctorRequest.doctorSpecialization());
        }
        if (!existingDoctor.getPhoneNumber().equals(updateDoctorRequest.phoneNumber())) {
            existingDoctor.setPhoneNumber(updateDoctorRequest.phoneNumber());
        }
        doctorRepository.save(existingDoctor);
        return modelMapper.map(existingDoctor, DoctorDTO.class);
    }
}
