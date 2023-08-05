package emil.dobrev.services.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.NonNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public class DoctorScheduleDTO {

    @NonNull
    private List<DayOfWeek> workingDays;

    @NonNull
    private LocalTime startTime;

    @NonNull
    private LocalTime endTime;
}
