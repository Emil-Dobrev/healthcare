package emil.dobrev.services.service.interfaces;

import lombok.*;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "users")
public record EmailMetaInformation(
        @NonNull String patientFullName,
        @NonNull String text,
        @NonNull String title,
        @NonNull String subject,
        @NonNull String email,
        @NonNull String header,
        @NonNull LocalDateTime timeOfAppointment
        ) {
}
