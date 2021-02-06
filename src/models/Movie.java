package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder
public class Movie {

    private final UUID id = UUID.randomUUID();
    private String title;
    private int releaseYear;
    private Set<String> genres;
}
