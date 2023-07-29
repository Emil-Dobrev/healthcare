package emil.dobrev.services.service;

import emil.dobrev.services.config.JwtService;
import emil.dobrev.services.dto.PatientRegistrationRequest;
import emil.dobrev.services.dto.RegistrationResponse;
import emil.dobrev.services.enums.Role;
import emil.dobrev.services.model.Patient;
import emil.dobrev.services.repository.PatientRepository;
import emil.dobrev.services.service.interfaces.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImp implements PatientService {

    private final PatientRepository patientRepository;
    private final JwtService jwtService;

    @Override
    public RegistrationResponse register(PatientRegistrationRequest patientRegistrationRequest) {
        var patient = Patient.builder()
                .email(patientRegistrationRequest.email())
                .firstName(patientRegistrationRequest.firstName())
                .lastName(patientRegistrationRequest.lastName())
                .password(patientRegistrationRequest.password())
                .roles(List.of(Role.PATIENT))
                .build();

        patientRepository.save(patient);
       var jwtToken = jwtService.generateToken(patient);

        return RegistrationResponse.builder()
                .message(jwtToken)
                .build();
    }
}
