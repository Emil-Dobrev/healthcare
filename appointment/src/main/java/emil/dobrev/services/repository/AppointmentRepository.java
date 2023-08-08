package emil.dobrev.services.repository;

import emil.dobrev.services.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAppointmentsByDoctorIdAndAppointmentDateTimeGreaterThanEqualAndAppointmentDateTimeLessThanEqual(
            Long doctorId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    );

    List<Appointment> findAppointmentsByDoctorIdAndAppointmentDateTimeBetween(
            Long doctorId,
            LocalDateTime atStartOfDay,
            LocalDateTime atTime);
}
