package emil.dobrev.services.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        String message,
        int status,
        String error
) {
    public ErrorResponse(String message, int status, String error) {
        this(LocalDateTime.now(), message, status, error);
    }

}
