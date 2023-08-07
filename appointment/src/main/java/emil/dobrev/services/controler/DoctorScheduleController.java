package emil.dobrev.services.controler;

import emil.dobrev.services.dto.DoctorScheduleDTO;
import emil.dobrev.services.dto.HolidayResponse;
import emil.dobrev.services.dto.HolidaysRequest;
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
    public ResponseEntity<List<HolidayResponse>> getAllHolidaysForDoctor(
            @RequestHeader("userId") Long userId,
            @RequestHeader("roles") String roles
    ) {
        log.info("Get all holidays for doctor with id: {}", userId);

        return ResponseEntity.ok()
                .body(doctorScheduleService.getAllHolidaysForDoctor(userId, roles));
    }

    @PostMapping("/holidays")
    public ResponseEntity<Void> setHolidays(
            @RequestHeader("userId") Long userId,
            @RequestHeader("roles") String roles,
            @RequestBody HolidaysRequest request
    ) {
        log.info("Add doctor holiday request");
        doctorScheduleService.setHolidays(userId, roles, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/holidays/{holidayId}")
    public ResponseEntity<Void> updateHolidays(
            @RequestHeader("userId") Long userId,
            @RequestHeader("roles") String roles,
            @PathVariable Long holidayId,
            @RequestBody HolidaysRequest request
    ) {
        log.info("Update holiday request with id: {}", holidayId);
        doctorScheduleService.updateHolidays(userId, roles, holidayId, request);
        return ResponseEntity.noContent().build();
    }

}
