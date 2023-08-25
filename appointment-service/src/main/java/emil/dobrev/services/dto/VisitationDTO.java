package emil.dobrev.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VisitationDTO {
    @NonNull
    private Long appointmentId;
    @NonNull
    private LocalDateTime visitDateTime;
    @NonNull
    private Long patientId;
    @NonNull
    private Long doctorId;
    @NonNull
    private String diagnosis;
    @NonNull
    private String treatmentPlan;
}


