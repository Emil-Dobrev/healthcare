package emil.dobrev.services.model;

import emil.dobrev.services.service.interfaces.EmailMetaInformation;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("notification")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Notification {

    public Notification(EmailMetaInformation emailMetaInformation, LocalDateTime timeOfAppointment) {
        this.emailMetaInformation = emailMetaInformation;
        this.timeOfAppointment = timeOfAppointment;
    }

    @Id
    String id;
    @NonNull
    EmailMetaInformation emailMetaInformation;
    @NonNull
    boolean isEmailSend = true;
    LocalDateTime timeOfAppointment;
}
