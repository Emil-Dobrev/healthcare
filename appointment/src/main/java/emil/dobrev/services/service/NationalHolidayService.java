package emil.dobrev.services.service;

import emil.dobrev.services.dto.HolidayApiResponse;
import emil.dobrev.services.enums.Country;
import emil.dobrev.services.model.QueryParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static emil.dobrev.services.constant.Constants.HTTPS_HOLIDAYAPI;

@Service
public class NationalHolidayService {
    @Value("${holidays.api.key}")
    private String apiKey;
    @Retryable(maxAttempts = 3 , backoff = @Backoff(delay = 1000)) // Retry 3 times with a 1-second delay
    public Optional<List<LocalDate>> getNationalHolidays(Country country) {
        QueryParams queryParams = new QueryParams();
        queryParams.key(apiKey);
        queryParams.country(country);
        // Free api only supports year < 2023
        queryParams.year(2022);

        RestTemplate restTemplate = new RestTemplate();
        String url = HTTPS_HOLIDAYAPI + "?" + queryParams;


        ResponseEntity<HolidayApiResponse> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        HolidayApiResponse.class
                );

        return Optional.of(Objects.requireNonNull(response.getBody()).getHolidays()
                .stream()
                .map(e -> LocalDate.parse(e.getDate()))
                .toList());
    }
}
