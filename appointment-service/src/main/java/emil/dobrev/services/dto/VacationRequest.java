package emil.dobrev.services.dto;

import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;

public record VacationRequest(@NonNull List<LocalDate> holidays) {
}
