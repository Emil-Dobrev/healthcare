package emil.dobrev.services.repository;

import emil.dobrev.services.model.MedicationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationScheduleRepository extends JpaRepository<MedicationSchedule, Long> {
}
