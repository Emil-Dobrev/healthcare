package emil.dobrev.services.service;

import emil.dobrev.services.config.JwtService;
import emil.dobrev.services.dto.AuthenticationResponse;
import emil.dobrev.services.dto.DoctorRegistrationRequest;
import emil.dobrev.services.dto.PatientRegistrationRequest;
import emil.dobrev.services.enums.DoctorSpecialization;
import emil.dobrev.services.enums.Role;
import emil.dobrev.services.model.Doctor;
import emil.dobrev.services.model.Patient;
import emil.dobrev.services.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImpTest {

    @InjectMocks
    private AuthenticationServiceImp authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void registerDoctor() {

        DoctorRegistrationRequest request = new DoctorRegistrationRequest(
                "test@example.com",
                "password",
                "John",
                "Doe",
                new Date(),  // Set a valid birthdate here
                DoctorSpecialization.CARDIOLOGY,
                "123456789",
                "123"
        );

        // Mock behavior of userRepository.save()
        Doctor savedDoctor = Doctor.builder()
                .id(1L)  // Set the expected ID here
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .specialization(request.specialization())
                .birthdate(request.birthdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .age(30)  // Set the expected age here
                .password("encodedPassword")  // Set the expected encoded password here
                .address(request.address())
                .roles(List.of(Role.DOCTOR))
                .build();
        when(userRepository.save(any())).thenReturn(savedDoctor);

        // Mock behavior of jwtService.generateToken()
        when(jwtService.generateToken(any())).thenReturn("mockedToken");

        // Perform the registration
        AuthenticationResponse response = authenticationService.register(request);

        // Assertions
        assertNotNull(response);
        assertEquals("mockedToken", response.token());
        verify(userRepository).save(any());  // Verify that userRepository.save() was called
        verify(jwtService).generateToken(any());  // Verify that jwtService.generateToken() was called
    }


    @Test
    void registerPatient() {
        // Arrange
        PatientRegistrationRequest request = new PatientRegistrationRequest(
                "patient@example.com",
                "password",
                "John",
                "Doe",
                new Date(1993, 07, 01),
                "1234567890"
        );

        Patient patient = Patient.builder()
                .id(1L)  // Set the expected ID here
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .birthdate(request.birthdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .age(30)  // Set the expected age here
                .password("encodedPassword")  // Set the expected encoded password here
                .roles(List.of(Role.DOCTOR))
                .build();

        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(patient);
        when(jwtService.generateToken(any())).thenReturn("jwtToken");

        // Act
        AuthenticationResponse response = authenticationService.register(request);

        // Assert
        // Perform assertions on the response if needed

        verify(userRepository).save(any());
        verify(jwtService).generateToken(any());
    }
}