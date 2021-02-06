package exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class DuplicateReviewException extends Exception {

    private final UUID userId;
    private final UUID movieId;

    public DuplicateReviewException(String msg, UUID userId, UUID movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }
}
