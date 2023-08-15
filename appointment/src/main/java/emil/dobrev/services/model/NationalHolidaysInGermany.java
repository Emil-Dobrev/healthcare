package emil.dobrev.services.model;

import java.time.LocalDate;
import java.util.List;

public class NationalHolidaysInGermany {
    private NationalHolidaysInGermany(){}

    public static List<LocalDate> getNationalHolidays() {
        return List.of(
                LocalDate.of(2023, 1, 1),   // New Year's Day
                LocalDate.of(2023, 4, 17),  // Easter Monday
                LocalDate.of(2023, 5, 1),   // Labour Day
                LocalDate.of(2023, 5, 25),  // Ascension Day
                LocalDate.of(2023, 10, 3),  // German Unity Day
                LocalDate.of(2023, 12, 25) // Christmas
        );
    }
}

