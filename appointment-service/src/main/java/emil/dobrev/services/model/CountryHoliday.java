package emil.dobrev.services.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonPropertyOrder({
        "name",
        "date",
        "observed",
        "public"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"country", "uuid", "weekday"})
@AllArgsConstructor
@Data
@NoArgsConstructor
public class CountryHoliday {

    private String name;

    private String date;

    private String observed;

    private Boolean isPublic;

}