package emil.dobrev.services.controller;

import emil.dobrev.services.dto.AuthenticationRequest;
import emil.dobrev.services.dto.PatientRegistrationRequest;
import emil.dobrev.services.dto.AuthenticationResponse;
import emil.dobrev.services.service.interfaces.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/patients")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PatientController {

    private final PatientService patientService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(patientService.authenticate(request));
    }

    @PostMapping
    public ResponseEntity<AuthenticationResponse> register(@RequestBody PatientRegistrationRequest patientRegistrationRequest) {
        log.info("Patient registration request");
        return ResponseEntity.ok().body(patientService.register(patientRegistrationRequest));
    }

}
