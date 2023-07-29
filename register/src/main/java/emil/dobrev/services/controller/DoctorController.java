package emil.dobrev.services.controller;

import emil.dobrev.services.dto.AuthenticationRequest;
import emil.dobrev.services.dto.DoctorDTO;
import emil.dobrev.services.dto.DoctorRegistrationRequest;
import emil.dobrev.services.dto.AuthenticationResponse;
import emil.dobrev.services.service.interfaces.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/doctors")
@RequiredArgsConstructor
@Slf4j
@Validated
public class DoctorController {
    private final DoctorService doctorService;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(doctorService.authenticate(request));
    }

    @PostMapping
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody DoctorRegistrationRequest doctorRegistrationRequest) {
        log.info("Doctor registration request");
        return ResponseEntity.ok().body(doctorService.register(doctorRegistrationRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        log.info("Get doctor by id: {}", id);
        return ResponseEntity.ok().body(doctorService.getDoctorById(id));
    }
}
