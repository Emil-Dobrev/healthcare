package emil.dobrev.services.repository;

import emil.dobrev.services.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

  Optional<DoctorSchedule> findByDoctorId(Long id);
  @Query(
         value = "SELECT h.holiday FROM doctor_schedule_holidays h WHERE h.doctor_schedule_id = :holidayId",
          nativeQuery = true
  )
  Optional<List<java.sql.Date>> findByHolidayId(@Param("holidayId") Long holidayId);
}
