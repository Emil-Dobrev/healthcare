package emil.dobrev.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateMedicationScheduleRequest {

    @NonNull
    private String name;
    @NonNull
    private double dosage;
    @NonNull
    private String dosageUnit;
    @NonNull
    private int frequency;
    @NonNull
    private LocalDate startDate;
    @NonNull
    private LocalDate endDate;
    @NonNull
    private int durationBetweenDoses;
    private LocalDateTime timeFirstDosageTaken;
}
