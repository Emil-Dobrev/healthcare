package emil.dobrev.services.service;

import emil.dobrev.services.dto.DoctorRegistrationRequest;
import emil.dobrev.services.dto.DoctorRegistrationResponse;
import emil.dobrev.services.entity.Doctor;
import emil.dobrev.services.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorRegistrationResponse register(DoctorRegistrationRequest doctorRegistrationRequest) {
        Doctor doctor = Doctor.builder()
                .email(doctorRegistrationRequest.email())
                .firstName(doctorRegistrationRequest.firstName())
                .lastName(doctorRegistrationRequest.lastName())
                .phoneNumber(doctorRegistrationRequest.phoneNumber())
                .specialization(doctorRegistrationRequest.specialization())
                .password(doctorRegistrationRequest.password())
                .build();

        doctorRepository.save(doctor);
        return DoctorRegistrationResponse.builder()
                .message("Successfully")
                .build();
    }
}
