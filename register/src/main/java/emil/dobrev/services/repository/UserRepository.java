package emil.dobrev.services.repository;

import emil.dobrev.services.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(@NonNull String email);
}
