package emil.dobrev.services.dto;

import lombok.NonNull;


public record AuthenticationRequest(
        @NonNull String email,
        @NonNull String password,
        @NonNull boolean isDoctor
) {
}
