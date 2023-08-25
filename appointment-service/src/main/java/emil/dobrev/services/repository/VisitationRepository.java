package emil.dobrev.services.repository;


import emil.dobrev.services.dto.VisitationDTO;
import emil.dobrev.services.model.VisitationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitationRepository extends JpaRepository<VisitationRecord, Long> {

    List<VisitationRecord> getAllByPatientId(Long id);
    Optional<VisitationRecord> findByAppointmentId(Long id);
}
