package emil.dobrev.services.dto;


import lombok.NonNull;

import java.time.LocalDateTime;

public record CommentDTO(
        @NonNull String createdBy,
        @NonNull LocalDateTime createdAt,
        @NonNull String description
) {

}
