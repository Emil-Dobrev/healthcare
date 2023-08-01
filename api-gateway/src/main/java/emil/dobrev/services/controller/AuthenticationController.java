package emil.dobrev.services.controller;

import emil.dobrev.services.dto.AuthenticationRequest;
import emil.dobrev.services.dto.AuthenticationResponse;
import emil.dobrev.services.dto.DoctorRegistrationRequest;
import emil.dobrev.services.dto.PatientRegistrationRequest;
import emil.dobrev.services.service.interfaces.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register/doctor")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody DoctorRegistrationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/register/patient")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody PatientRegistrationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
