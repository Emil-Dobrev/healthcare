package emil.dobrev.services.dto;


import java.sql.Date;
import java.util.List;

public record VacationResponse(
        Long holidayId,
        List<Date> holidayDate) {
}
