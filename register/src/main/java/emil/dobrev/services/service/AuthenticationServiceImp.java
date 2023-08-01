package emil.dobrev.services.service;


import emil.dobrev.services.config.JwtService;
import emil.dobrev.services.dto.AuthenticationRequest;
import emil.dobrev.services.dto.AuthenticationResponse;
import emil.dobrev.services.dto.DoctorRegistrationRequest;
import emil.dobrev.services.dto.PatientRegistrationRequest;
import emil.dobrev.services.enums.Role;
import emil.dobrev.services.model.Doctor;
import emil.dobrev.services.model.Patient;
import emil.dobrev.services.repository.DoctorRepository;
import emil.dobrev.services.repository.PatientRepository;
import emil.dobrev.services.service.interfaces.AuthenticationService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(DoctorRegistrationRequest doctorRegistrationRequest) {
        var doctor = Doctor.builder()
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
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

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

        String jwtToken = getToken(request);
        return new AuthenticationResponse(jwtToken);
    }

    private String getToken(AuthenticationRequest request) {
        UserDetails user;
        if(request.isDoctor()) {
             user = doctorRepository.findByEmail(request.email())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } else {
             user = patientRepository.findByEmail(request.email())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }

        return jwtService.generateToken(user);
    }
}
