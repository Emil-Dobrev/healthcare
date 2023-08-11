package emil.dobrev.services.dto;

import java.sql.Date;
import java.time.LocalDate;

public interface Holiday {

    Long getHolidayId();
    Date getHolidayDate();
    LocalDate getHolidayLocalDate();
}
