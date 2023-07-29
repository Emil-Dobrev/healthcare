package emil.dobrev.services.service;

import emil.dobrev.services.config.JwtService;
import emil.dobrev.services.dto.DoctorDTO;
import emil.dobrev.services.dto.DoctorRegistrationRequest;
import emil.dobrev.services.dto.RegistrationResponse;
import emil.dobrev.services.enums.Role;
import emil.dobrev.services.model.Doctor;
import emil.dobrev.services.exception.NoSuchElementException;
import emil.dobrev.services.repository.DoctorRepository;
import emil.dobrev.services.service.interfaces.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImp implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @Override
    public RegistrationResponse register(DoctorRegistrationRequest doctorRegistrationRequest) {
        Doctor doctor = Doctor.builder()
                .email(doctorRegistrationRequest.email())
                .firstName(doctorRegistrationRequest.firstName())
                .lastName(doctorRegistrationRequest.lastName())
                .phoneNumber(doctorRegistrationRequest.phoneNumber())
                .specialization(doctorRegistrationRequest.specialization())
                .password(passwordEncoder.encode(doctorRegistrationRequest.password()))
                .roles(List.of(Role.DOCTOR))
                .build();

        var jwtToken = jwtService.generateToken(doctor);

        doctorRepository.save(doctor);
        return RegistrationResponse.builder()
                .message(jwtToken)
                .build();
    }

    @Override
    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No doctor with id: " + id));

        return modelMapper.map(doctor, DoctorDTO.class);
    }
}
