package emil.dobrev.services.controller;

import emil.dobrev.services.dto.DoctorDTO;
import emil.dobrev.services.dto.UpdateDoctorRequest;
import emil.dobrev.services.enums.DoctorSpecialization;
import emil.dobrev.services.service.interfaces.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
@Slf4j
@Validated
public class DoctorController {
    private final DoctorService doctorService;


    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        log.info("Get doctor by id: {}", id);
        return ResponseEntity.ok().body(doctorService.getDoctorById(id));
    }

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors(
            @RequestParam(name = "filter", required = false) DoctorSpecialization specialization
    ) {
        log.info("Get all doctors request");
        return ResponseEntity.ok().body(doctorService.getAllDoctors(specialization));
    }

    @PatchMapping
    public ResponseEntity<DoctorDTO> updateDoctor(@RequestHeader("userId") Long id,
                                                  @RequestBody UpdateDoctorRequest updateDoctorRequest) {
        log.info("Update doctor request");
        return ResponseEntity.ok().body(doctorService.updateDoctor(id, updateDoctorRequest));
    }
}
