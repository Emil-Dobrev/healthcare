package emil.dobrev.services.dto;

import lombok.NonNull;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        @NonNull
        LocalDateTime appointmentDateTime,
        @NonNull
        Long doctorId,
        @NonNull
        Long patientId) {
}
