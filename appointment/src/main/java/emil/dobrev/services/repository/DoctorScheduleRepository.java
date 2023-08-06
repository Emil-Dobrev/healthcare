package emil.dobrev.services.repository;

import emil.dobrev.services.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

  Optional<DoctorSchedule> findByDoctorId(Long id);

  @Modifying
  @Query("DELETE FROM DoctorHoliday d WHERE d.id = :holidayId")
  void deleteHolidayDates(@Param("holidayId") Long holidayId);

  @Modifying
  @Query(
         value = "UPDATE doctor_holiday_dates h SET h.holiday_date IN :newHolidayDates WHERE h.holiday_id = :holidayId",
          nativeQuery = true
  )
  Optional<List<java.sql.Date>> updateHoliday(@Param("holidayId") Long holidayId,
                                              @Param("newHolidayDates")List<LocalDate> newHolidayDates);
}
