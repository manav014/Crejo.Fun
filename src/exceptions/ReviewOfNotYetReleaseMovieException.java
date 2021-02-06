package exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ReviewOfNotYetReleaseMovieException extends Exception {

    private final UUID movieId;

    public ReviewOfNotYetReleaseMovieException(String msg, UUID movieId) {
        this.movieId = movieId;
    }
}
