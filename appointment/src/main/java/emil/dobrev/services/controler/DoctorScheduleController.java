package emil.dobrev.services.controler;

import emil.dobrev.services.dto.CreateScheduleRequest;
import emil.dobrev.services.dto.DoctorScheduleDTO;
import emil.dobrev.services.service.interfaces.DoctorScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule")
@Slf4j
public record DoctorScheduleController(
        DoctorScheduleService doctorScheduleService
) {


    @GetMapping("/{id}")
    public ResponseEntity<DoctorScheduleDTO> getSchedule(@PathVariable Long id) {
        log.info("Get doctor schedule request");
        return ResponseEntity.ok().body(doctorScheduleService.getSchedule(id));
    }

    @PostMapping
    public ResponseEntity<Void> createSchedule(
            @RequestHeader("userId") Long id,
            @RequestHeader("roles") String roles,
            @RequestBody CreateScheduleRequest request
    ) {
        log.info("Create doctor schedule request");
        doctorScheduleService.createSchedule(id, roles, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/holidays")
    public ResponseEntity<Void> setHolidays(
            @RequestHeader("userId") Long id,
            @RequestHeader("roles") String roles,
            @RequestBody List<LocalDate> holidays
    ) {
        log.info("Add doctor holiday request");
        doctorScheduleService.setHoliday(id, roles, holidays);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/holidays/{id}")
    public ResponseEntity<Void> updateHolidays(
            @RequestHeader("userId") Long id,
            @RequestHeader("roles") String roles,
            @PathVariable Long holidayId,
            @RequestBody List<LocalDate> holidays
    ) {
        return null;
    }

}
