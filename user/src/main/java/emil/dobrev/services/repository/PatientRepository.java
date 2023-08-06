package emil.dobrev.services.repository;

import emil.dobrev.services.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query(
            value = "SELECT u.* FROM USERS u WHERE 'PATIENT' = ANY(u.roles)",
            nativeQuery = true
    )
    List<Patient> findAll();
}
