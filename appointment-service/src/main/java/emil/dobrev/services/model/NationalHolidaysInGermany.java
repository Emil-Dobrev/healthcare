package emil.dobrev.services.model;

import emil.dobrev.services.enums.Country;
import emil.dobrev.services.service.NationalHolidayService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class NationalHolidaysInGermany {

    private final NationalHolidayService nationalHolidayService;
    public static List<LocalDate> nationalHolidaysInGermany;

    private NationalHolidaysInGermany(NationalHolidayService nationalHolidayService) {
        this.nationalHolidayService = nationalHolidayService;
        setNationalHolidaysInGermany();
    }


    public void setNationalHolidaysInGermany() {
        nationalHolidaysInGermany = this.nationalHolidayService.getNationalHolidays(Country.GERMANY)
                .orElse(getNationalHolidays());
    }

    private   List<LocalDate> getNationalHolidays() {
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

