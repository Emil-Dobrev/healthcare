package emil.dobrev.services.repository;

import emil.dobrev.services.dto.DoctorScheduleDTO;
import emil.dobrev.services.dto.Holiday;
import emil.dobrev.services.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    Optional<DoctorSchedule> findByDoctorId(Long id);

    @Modifying
    @Query("UPDATE DoctorSchedule d " +
            "SET d.workingDays = :workingDays, " +
            "d.startTime = :startTime, " +
            "d.endTime = :endTime, " +
            "d.breakFrom = :breakFrom, " +
            "d.breakTo = :breakTo " +
            "WHERE d.id = :scheduleId")
    DoctorScheduleDTO updateSchedule(@Param("scheduleId") Long scheduleId,
                                     @Param("workingDays") List<DayOfWeek> workingDays,
                                     @Param("startTime") LocalTime startTime,
                                     @Param("endTime") LocalTime endTime,
                                     @Param("breakFrom") LocalTime breakFrom,
                                     @Param("breakTo") LocalTime breakTo);

    @Query(value =
            "SELECT dh.holiday_id as holidayId, dh.holiday_date as holidayDate" +
                    " FROM doctor_holiday_dates dh WHERE dh.holiday_id IN " +
            "(SELECT id FROM doctor_holidays WHERE schedule_id = :scheduleId)",
            nativeQuery = true)
    Optional<List<Holiday>> getAllHolidaysForDoctor(@Param("scheduleId") Long scheduleId);

    @Modifying
    @Query("DELETE FROM DoctorHoliday d WHERE d.id = :holidayId")
    void deleteHolidayDates(@Param("holidayId") Long holidayId);

    @Query(value = "SELECT dh.holiday_id as holidayId, dh.holiday_date as holidayDate " +
            "FROM doctor_holiday_dates dh where dh.holiday_id = :holidayId",
    nativeQuery = true)
    Optional<List<Holiday>> getHolidayById(@Param("holidayId") Long holidayId);

}
