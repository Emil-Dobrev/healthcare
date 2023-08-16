package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.dto.VisitationDTO;
import emil.dobrev.services.dto.VisitationRequest;

import java.util.List;

public interface VisitationService {
    VisitationDTO addVisitation(VisitationRequest visitationRequest, Long doctorId, String roles);

    List<VisitationDTO> getAllVisitationsForPatient(Long userId, String roles, Long patientId);
}
