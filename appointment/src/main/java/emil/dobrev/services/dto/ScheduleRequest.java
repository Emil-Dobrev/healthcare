package emil.dobrev.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NonNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ScheduleRequest(
        @NonNull List<DayOfWeek> workingDays,
        @NonNull LocalTime startTime,
        @NonNull LocalTime endTime,
        @NonNull LocalTime breakFrom,
        @NonNull LocalTime breakTo
) {
}
