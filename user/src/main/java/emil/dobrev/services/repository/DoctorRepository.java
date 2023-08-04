package emil.dobrev.services.repository;

import emil.dobrev.services.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query(
            value = "SELECT u.* FROM USERS u WHERE 'DOCTOR' = ANY(u.roles)",
            nativeQuery = true
    )
    List<Doctor> findAll();
}