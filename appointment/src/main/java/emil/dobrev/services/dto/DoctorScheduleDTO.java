package emil.dobrev.services.dto;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorScheduleDTO {

    @NonNull
    private List<DayOfWeek> workingDays;

    @NonNull
    private LocalTime startTime;

    @NonNull
    private LocalTime endTime;
}
