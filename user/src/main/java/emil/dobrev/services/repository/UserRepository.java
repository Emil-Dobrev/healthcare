package emil.dobrev.services.repository;

import emil.dobrev.services.model.Patient;
import emil.dobrev.services.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<Patient> findByEmail(String email);
}
