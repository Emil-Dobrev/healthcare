package emil.dobrev.services.service;

import emil.dobrev.services.config.JwtService;
import emil.dobrev.services.dto.AuthenticationRequest;
import emil.dobrev.services.dto.AuthenticationResponse;
import emil.dobrev.services.dto.PatientRegistrationRequest;
import emil.dobrev.services.enums.Role;
import emil.dobrev.services.model.Patient;
import emil.dobrev.services.repository.PatientRepository;
import emil.dobrev.services.service.interfaces.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImp implements PatientService {

    private final PatientRepository patientRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(PatientRegistrationRequest patientRegistrationRequest) {
        var patient = Patient.builder()
                .email(patientRegistrationRequest.email())
                .firstName(patientRegistrationRequest.firstName())
                .lastName(patientRegistrationRequest.lastName())
                .password(passwordEncoder.encode(patientRegistrationRequest.password()))
                .roles(List.of(Role.PATIENT))
                .build();

        patientRepository.save(patient);
        var jwtToken = jwtService.generateToken(patient);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                );
        authenticationManager.authenticate(authenticationToken);

        var user = patientRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }
}
