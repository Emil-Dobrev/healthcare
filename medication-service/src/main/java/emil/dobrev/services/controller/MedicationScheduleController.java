package emil.dobrev.services.controller;

import emil.dobrev.services.dto.MedicationScheduleDTO;
import emil.dobrev.services.model.MedicationSchedule;
import emil.dobrev.services.service.interfaces.MedicationScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody MedicationScheduleDTO medicationScheduleDTO
    ) {
        log.info("Create medication schedule for user with id: {}", userId);
        medicationScheduleService.createMedicationSchedule(userId, roles, medicationScheduleDTO);
        return ResponseEntity.noContent().build();
    }
}
