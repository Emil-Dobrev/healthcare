package emil.dobrev.services.dto;

import lombok.*;

import java.time.DayOfWeek;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private String startDate;
    @NonNull
    private String endDate;
    @NonNull
    private List<DayOfWeek> daysOfWeek;
    @NonNull
    private int durationBetweenDoses;
}
