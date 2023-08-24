package emil.dobrev.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicationScheduleDTO {
    @NonNull
    private Long userId;
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
    private int dosageTakenToday;
    private LocalDateTime timeFirstDosageTaken;
}
