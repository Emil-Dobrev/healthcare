package emil.dobrev.services.dto;

import lombok.NonNull;

public record CommentRequest(
        @NonNull String description
) {
}
