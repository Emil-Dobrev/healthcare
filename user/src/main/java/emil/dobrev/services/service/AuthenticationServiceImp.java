package emil.dobrev.services.service;


import emil.dobrev.services.config.JwtService;
import emil.dobrev.services.dto.AuthenticationRequest;
import emil.dobrev.services.dto.AuthenticationResponse;
import emil.dobrev.services.dto.DoctorRegistrationRequest;
import emil.dobrev.services.dto.PatientRegistrationRequest;
import emil.dobrev.services.enums.Role;
import emil.dobrev.services.model.Doctor;
import emil.dobrev.services.model.Patient;
import emil.dobrev.services.model.User;
import emil.dobrev.services.repository.UserRepository;
import emil.dobrev.services.service.interfaces.AuthenticationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(DoctorRegistrationRequest doctorRegistrationRequest) {
        LocalDate birthDate = doctorRegistrationRequest.birthdate().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate();
        var age = Period.between(birthDate, LocalDate.now()).getYears();
        var doctor = Doctor.builder()
                .email(doctorRegistrationRequest.email())
                .firstName(doctorRegistrationRequest.firstName())
                .lastName(doctorRegistrationRequest.lastName())
                .phoneNumber(doctorRegistrationRequest.phoneNumber())
                .specialization(doctorRegistrationRequest.specialization())
                .birthdate(birthDate)
                .age(age)
                .password(passwordEncoder.encode(doctorRegistrationRequest.password()))
                .address(doctorRegistrationRequest.address())
                .roles(List.of(Role.DOCTOR))
                .build();

        doctor = userRepository.save(doctor);

        var jwtToken = jwtService.generateToken(doctor);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    @Override
    public AuthenticationResponse register(PatientRegistrationRequest patientRegistrationRequest) {
        LocalDate birthDate = patientRegistrationRequest.birthdate().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate();
        var age = Period.between(birthDate, LocalDate.now()).getYears();
        var patient = Patient.builder()
                .email(patientRegistrationRequest.email())
                .firstName(patientRegistrationRequest.firstName())
                .lastName(patientRegistrationRequest.lastName())
                .password(passwordEncoder.encode(patientRegistrationRequest.password()))
                .birthdate(birthDate)
                .age(age)
                .roles(List.of(Role.PATIENT))
                .phoneNumber(patientRegistrationRequest.phoneNumber())
                .build();

        userRepository.save(patient);

        var jwtToken = jwtService.generateToken(patient);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(@NonNull AuthenticationRequest request) {
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
        User user;

        user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        return jwtService.generateToken(user);
    }
}
