package emil.dobrev.services.controller;

import emil.dobrev.services.dto.CreateMedicationScheduleRequest;
import emil.dobrev.services.dto.MedicationScheduleDTO;
import emil.dobrev.services.model.MedicationSchedule;
import emil.dobrev.services.service.interfaces.MedicationScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/medication")
@RequiredArgsConstructor
@Slf4j
public class MedicationScheduleController {

    private final MedicationScheduleService medicationScheduleService;

    @PostMapping
    public ResponseEntity<MedicationSchedule> createMedicationSchedule(
            @RequestHeader("userId") Long userId,
            @RequestHeader("roles") String roles,
            @RequestBody CreateMedicationScheduleRequest request
    ) {
        log.info("Create medication schedule for user with id: {}", userId);
        medicationScheduleService.createMedicationSchedule(userId, roles, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MedicationScheduleDTO>> getAllMedicationScheduleForUser(
            @RequestHeader("userId") Long userId,
            @RequestHeader("roles") String roles
    ) {
        log.info("Getting all medication schedules for user with id: {}", userId);
        return ResponseEntity.ok()
                .body(medicationScheduleService.getAllMedicationScheduleForUser(userId, roles));
    }

    @PatchMapping
    public ResponseEntity<Void> setTimeFirstDosageTaken(
            @RequestHeader("userId") Long userId,
            @RequestHeader("roles") String roles,
            @RequestParam("medicationScheduleId") Long medicationScheduleId,
            @RequestParam("time") LocalDateTime timeFirstDosageTaken
    ) {
        log.info("Setting time for first dosage for medication schedule with id: {} ", medicationScheduleId);
        medicationScheduleService.setTimeFirstDosageTaken(userId, roles, medicationScheduleId, timeFirstDosageTaken);
        return ResponseEntity.noContent().build();
    }
}
