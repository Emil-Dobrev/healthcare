package emil.dobrev.services.utils;

import emil.dobrev.services.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;


public class JpaRepositoryUtils {


    public  static <T> T getOrThrow(JpaRepository<T, Long> repository, Long id)  {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Object not found with id: " + id));
    }
}
