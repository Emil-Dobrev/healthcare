package emil.dobrev.services.controler;

import emil.dobrev.services.dto.VisitationDTO;
import emil.dobrev.services.dto.VisitationRequest;
import emil.dobrev.services.service.interfaces.VisitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/visitation")
public class VisitationController {

    private final VisitationService visitationService;
    @PostMapping
    public ResponseEntity<VisitationDTO> addVisitation(
            @RequestBody VisitationRequest request,
            @RequestHeader("userId") Long userId,
            @RequestHeader("roles") String roles
    ) {
        return ResponseEntity.ok().body(visitationService.addVisitation(request,userId, roles));
    }

    @GetMapping
    public ResponseEntity<List<VisitationDTO>> getAllVisitationsForPatient(
            @RequestHeader("userId") Long userId,
            @RequestHeader("roles") String roles,
            @RequestParam("patientId") Long patientId
    ) {
        return ResponseEntity.ok().body(
               visitationService.getAllVisitationsForPatient(userId, roles, patientId)
        );
    }

}
