package emil.dobrev.services.dto;

import lombok.NonNull;

import java.time.LocalDateTime;

public record UpdateAppointmentRequest(
        @NonNull Long appointmentId,
        @NonNull LocalDateTime newAppointmentTime
        ) {
}
