package emil.dobrev.services.controler;

import emil.dobrev.services.dto.AppointmentResponse;
import emil.dobrev.services.dto.CreateAppointmentRequest;
import emil.dobrev.services.service.interfaces.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/appointment")
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/{doctorId}")
    public ResponseEntity<Void> getAllAvailableSlots(
            @PathVariable Long doctorId,
            @RequestHeader("roles") String roles,
            @RequestBody LocalDate requestedDate
    ) {
        log.info("Getting all appointments for doctor request");
        appointmentService.getAllAvailableSlots(doctorId, roles, requestedDate);
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(
            @RequestBody CreateAppointmentRequest createAppointmentRequest,
            @RequestHeader("userId") Long patientId,
            @RequestHeader("roles") String roles
    ) {
        log.info("Creating appointment for doctor request");

        return ResponseEntity.ok()
                .body(appointmentService.create(createAppointmentRequest, patientId, roles));
    }

}
