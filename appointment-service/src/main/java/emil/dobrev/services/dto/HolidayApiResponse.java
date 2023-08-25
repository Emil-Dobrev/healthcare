package emil.dobrev.services.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import emil.dobrev.services.model.CountryHoliday;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * This class represents the complete JSON response of the API <code>/v1/holidays</code>
 */
@JsonPropertyOrder({
        "status",
        "error",
        "warning",
        "requests",
        "holidays"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class HolidayApiResponse {

    @JsonProperty("status")
    @Getter
    @Setter
    private Integer status;

    @JsonProperty("holidays")
    @Getter
    @Setter
    private List<CountryHoliday> holidays;

    @JsonProperty("error")
    @Getter
    @Setter
    private String error;

    @JsonProperty("warning")
    @Getter
    @Setter
    private String warning;

    @Override
    public String toString() {
        return "HolidayResponse{" +
                "status=" + status +
                ", holidays=" + holidays +
                ", error='" + (error == null ? "" : error) + '\'' +
                ", warning='" + (warning == null ? "" : warning) + '\'' +
                '}';
    }
}