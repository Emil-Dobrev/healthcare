package emil.dobrev.services.service.interfaces;

import lombok.NonNull;

import java.time.LocalDateTime;


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
