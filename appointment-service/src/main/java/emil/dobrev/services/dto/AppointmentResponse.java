package emil.dobrev.services.dto;

import java.time.LocalDateTime;

public record AppointmentResponse(
        LocalDateTime appointmentDateTime,
        LocalDateTime endOFAppointmentDateTime
) {
}
