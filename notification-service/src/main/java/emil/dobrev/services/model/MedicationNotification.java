package emil.dobrev.services.model;

import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MedicationNotification(
        @NonNull Long userId,
        @NonNull String name,
        @NonNull
        double dosage,
        @NonNull
        String dosageUnit,
        @NonNull
        int frequencyPerDay,
        @NonNull
        LocalDate startDate,
        @NonNull
        LocalDate endDate,
        @NonNull
        int durationInHoursBetweenDoses,
        @NonNull
        LocalDateTime firstDosage,
        @NonNull
        LocalDateTime nextDosage
) {
}
