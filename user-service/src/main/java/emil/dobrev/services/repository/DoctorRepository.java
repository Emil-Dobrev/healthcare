package emil.dobrev.services.repository;

import emil.dobrev.services.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query(
            value = "SELECT u.* FROM USERS u WHERE 'DOCTOR' = ANY(u.roles)" +
                    "AND (:specialization IS NULL OR u.specialization = :specialization)",
            nativeQuery = true
    )
    List<Doctor> findAll(@Param("specialization") String specialization);

    Optional<Doctor> findByEmail(String email);
}
