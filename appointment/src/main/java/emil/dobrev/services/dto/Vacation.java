package emil.dobrev.services.dto;

import java.sql.Date;
import java.time.LocalDate;

public interface Vacation {

    Long getVacationId();
    Date getVacationDate();
    LocalDate getVacationLocalDate();
}
