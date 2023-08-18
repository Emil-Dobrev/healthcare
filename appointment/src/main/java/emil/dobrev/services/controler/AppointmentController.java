package emil.dobrev.services.controler;

import emil.dobrev.services.dto.AppointmentResponse;
import emil.dobrev.services.dto.CreateAppointmentRequest;
import emil.dobrev.services.dto.TimeSlot;
import emil.dobrev.services.dto.UpdateAppointmentRequest;
import emil.dobrev.services.service.interfaces.AppointmentService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/appointment")
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/{doctorId}")
    public ResponseEntity<List<TimeSlot>> getAllAvailableSlots(
            @PathVariable Long doctorId,
            @RequestHeader("roles") String roles,
            @RequestBody LocalDate requestedDate
    ) {
        log.info("Getting all appointments for doctor request");

        return ResponseEntity.ok()
                .body(appointmentService.getAllAvailableSlots(doctorId, roles, requestedDate));
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

    @DeleteMapping
    public ResponseEntity<HttpStatus> cancelAppointment(
            @NonNull @RequestParam("appointmentId") Long appointmentId,
            @RequestHeader("userId") Long patientId,
            @RequestHeader("roles") String roles
    ) {
        log.info("Cancel appointment with id: {}", appointmentId);
        appointmentService.deleteAppointment(appointmentId, patientId, roles);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<AppointmentResponse> updateAppointment(
            @RequestBody UpdateAppointmentRequest updateAppointmentRequest,
            @RequestHeader("userId") Long patientId,
            @RequestHeader("roles") String roles
    ) {
        log.info("Updating appointment with id: {}", updateAppointmentRequest.appointmentId());
        return ResponseEntity.ok()
                .body(appointmentService.updateAppointment(updateAppointmentRequest,patientId,roles));
    }

}
