package emil.dobrev.services.dto;


import lombok.NonNull;

public record VoteRequest(

       @NonNull Double rating
) {
    public VoteRequest {
        if (rating < 1.0 || rating > 5.0) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }
}
