package emil.dobrev.services.repository;

import emil.dobrev.services.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findAllByAppointmentDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
