package emil.dobrev.services.controller;

import emil.dobrev.services.dto.*;
import emil.dobrev.services.enums.DoctorSpecialization;
import emil.dobrev.services.service.interfaces.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
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
    @Cacheable(value = "doctors")
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

    @GetMapping("/comment/{doctorId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForDoctor(
            @PathVariable Long doctorId
    ) {
        log.info("Get comments for doctor with id: {}", doctorId);
        return ResponseEntity.ok()
                .body(doctorService.getAllCommentsForDoctor(doctorId));
    }

    @PostMapping("/vote")
    public ResponseEntity<HttpStatus> voteForDoctor(
            @RequestParam("doctorId") Long doctorId,
            @RequestHeader("userId") Long patientId,
            @RequestBody VoteRequest request
    ) {
        log.info("Voting for doctor with id: {}", doctorId);
        doctorService.voteForDoctor(doctorId, patientId, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentDTO> addComment(
            @RequestParam("doctorId") Long doctorId,
            @RequestHeader("userId") Long patientId,
            @RequestBody CommentRequest request

    ) {
        log.info("Add comment for doctor with id: {}", doctorId);
        return ResponseEntity.ok().body(doctorService.addComment(doctorId, patientId, request));
    }
}
