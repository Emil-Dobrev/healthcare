package emil.dobrev.services.controler;

import emil.dobrev.services.dto.CreateAppointmentRequest;
import emil.dobrev.services.service.interfaces.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/appointment")
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
//    @PreAuthorize("hasAnyAuthority(PATIENT)")
    public ResponseEntity<HttpStatus> createAppointment(@RequestBody CreateAppointmentRequest createAppointmentRequest) {
        log.info("Creating appointment for doctor request");
        appointmentService.create(createAppointmentRequest);
     return ResponseEntity.noContent().build();
    }

//    @PostMapping void createDoctorSchedule(Authentication authentication) {
//
//    }
}
