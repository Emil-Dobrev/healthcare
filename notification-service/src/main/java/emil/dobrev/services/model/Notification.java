package emil.dobrev.services.model;

import emil.dobrev.services.service.interfaces.EmailMetaInformation;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("notification")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Notification {

    public Notification(EmailMetaInformation emailMetaInformation) {
        this.emailMetaInformation = emailMetaInformation;
    }

    @Id
    String id;
    @NonNull
    EmailMetaInformation emailMetaInformation;
    @NonNull
    boolean isSend = true;
}
