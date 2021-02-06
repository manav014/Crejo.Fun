package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class Review {

    private final UUID id = UUID.randomUUID();
    @Setter
    private int score;
    private UUID user;
    private UUID movie;

    Review(int score, UUID user, UUID movie) {
        if(score < 1 || score > 10) throw new IllegalArgumentException("illegal score passed");
        this.score = score;
        this.user = user;
        this.movie = movie;
    }
}
