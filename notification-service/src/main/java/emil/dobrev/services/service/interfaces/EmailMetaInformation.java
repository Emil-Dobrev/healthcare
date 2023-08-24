package emil.dobrev.services.service.interfaces;

import lombok.NonNull;


public record EmailMetaInformation(
        @NonNull String userFullName,
        @NonNull String text,
        @NonNull String title,
        @NonNull String subject,
        @NonNull String email,
        @NonNull String header
) {
}
