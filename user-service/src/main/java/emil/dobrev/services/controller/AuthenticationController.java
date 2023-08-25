package emil.dobrev.services.controller;

import emil.dobrev.services.dto.AuthenticationRequest;
import emil.dobrev.services.dto.AuthenticationResponse;
import emil.dobrev.services.dto.DoctorRegistrationRequest;
import emil.dobrev.services.dto.PatientRegistrationRequest;
import emil.dobrev.services.service.interfaces.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register/doctor")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody DoctorRegistrationRequest request) {
        log.info("Register doctor request");
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/register/patient")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody PatientRegistrationRequest request) {
        log.info("Register doctor patient");
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        log.info("Login request");
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
