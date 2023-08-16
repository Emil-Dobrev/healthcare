package emil.dobrev.services.dto;

import lombok.NonNull;

public record VisitationRequest(
        @NonNull
        Long appointmentId,
        @NonNull
        String diagnosis,
        @NonNull
        String treatmentPlan
) {
}
