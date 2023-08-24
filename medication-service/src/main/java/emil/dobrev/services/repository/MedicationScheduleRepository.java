package emil.dobrev.services.repository;

import emil.dobrev.services.model.MedicationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicationScheduleRepository extends JpaRepository<MedicationSchedule, Long> {

    Optional<List<MedicationSchedule>> findAllByUserId(Long userId);

    List<MedicationSchedule> findAllByIsActive(boolean isActive);
}
