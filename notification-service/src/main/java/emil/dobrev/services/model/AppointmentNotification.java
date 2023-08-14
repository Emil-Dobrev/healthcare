package emil.dobrev.services.model;

import lombok.NonNull;

import java.time.LocalDateTime;

public record AppointmentNotification(
        @NonNull Long appointmentId,
        @NonNull Long patientId,
        @NonNull Long doctorId,
        @NonNull LocalDateTime appointmentDateTime,
        @NonNull LocalDateTime endOFAppointmentDateTime
) {
}
