package emil.dobrev.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record RegistrationResponse(
        @JsonProperty("access_token") String token
) {
}
