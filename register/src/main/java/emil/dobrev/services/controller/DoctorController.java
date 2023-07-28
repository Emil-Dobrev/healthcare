package emil.dobrev.services.controller;

import emil.dobrev.services.dto.DoctorRegistrationRequest;
import emil.dobrev.services.dto.DoctorRegistrationResponse;
import emil.dobrev.services.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/doctors")
@RequiredArgsConstructor
@Slf4j
@Validated
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping
    public DoctorRegistrationResponse register(@Valid @RequestBody DoctorRegistrationRequest doctorRegistrationRequest) {
        log.info("new doctor registration {}", doctorRegistrationRequest);
        return doctorService.register(doctorRegistrationRequest);
    }
}
