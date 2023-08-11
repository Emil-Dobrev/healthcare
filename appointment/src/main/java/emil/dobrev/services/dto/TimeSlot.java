package emil.dobrev.services.dto;

import lombok.NonNull;

import java.time.LocalDateTime;

public record TimeSlot(
        @NonNull LocalDateTime start,
        @NonNull LocalDateTime end
) {
}
