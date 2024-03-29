package emil.dobrev.services.controler;

import emil.dobrev.services.dto.DoctorScheduleDTO;
import emil.dobrev.services.dto.VacationResponse;
import emil.dobrev.services.dto.VacationRequest;
import emil.dobrev.services.dto.ScheduleRequest;
import emil.dobrev.services.service.interfaces.DoctorScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule")
@Slf4j
public record DoctorScheduleController(
        DoctorScheduleService doctorScheduleService
) {


    @GetMapping("/{id}")
    public ResponseEntity<DoctorScheduleDTO> getSchedule(@PathVariable Long userId) {
        log.info("Get doctor schedule request");
        return ResponseEntity.ok().body(doctorScheduleService.getSchedule(userId));
    }

    @PostMapping
    public ResponseEntity<Void> createSchedule(
            @RequestHeader("userId") Long userId,
            @RequestHeader("roles") String roles,
            @RequestBody ScheduleRequest request
    ) {
        log.info("Create doctor schedule request");
        doctorScheduleService.createSchedule(userId, roles, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{scheduleID}")
    public ResponseEntity<DoctorScheduleDTO> updateSchedule(
            @RequestHeader("userId") Long userId,
            @RequestHeader("roles") String roles,
            @PathVariable Long scheduleID,
            @RequestBody ScheduleRequest request
    ) {
        log.info("Update doctor schedule request");
        return ResponseEntity.ok()
                .body(doctorScheduleService.updateSchedule(userId, roles, scheduleID, request));
    }

    @GetMapping("/holidays")
    public ResponseEntity<List<VacationResponse>> getALlVacationsForDoctor(
            @RequestHeader("userId") Long userId,
            @RequestHeader("roles") String roles
    ) {
        log.info("Get all holidays for doctor with id: {}", userId);

        return ResponseEntity.ok()
                .body(doctorScheduleService.getALlVacationsForDoctor(userId, roles));
    }

    @PostMapping("/holidays")
    public ResponseEntity<Void> createVacation(
            @RequestHeader("userId") Long userId,
            @RequestHeader("roles") String roles,
            @RequestBody VacationRequest request
    ) {
        log.info("Add doctor holiday request");
        doctorScheduleService.createVacation(userId, roles, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/holidays/{holidayId}")
    public ResponseEntity<Void> updateVacation(
            @RequestHeader("userId") Long userId,
            @RequestHeader("roles") String roles,
            @PathVariable Long holidayId,
            @RequestBody VacationRequest request
    ) {
        log.info("Update holiday request with id: {}", holidayId);
        doctorScheduleService.updateVacation(userId, roles, holidayId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/holidays/{holidayId}")
    public ResponseEntity<VacationResponse> getVacationById(
            @RequestHeader("userId") Long userId,
            @RequestHeader("roles") String roles,
            @PathVariable Long holidayId
    ) {
        log.info("Get holiday by id: {}", holidayId);

        return ResponseEntity.ok()
                .body(doctorScheduleService.getVacationById(userId, roles, holidayId));
    }
}
