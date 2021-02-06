package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class User {

    private final UUID id = UUID.randomUUID();
    private String name;
    @Setter
    private Level userLevel = Level.VIEWER;

    public User(String name) {
        this.name = name;
    }

    public enum Level {
        VIEWER(1), CRITIC(2);

        @Getter
        private int order;

        Level(int val) {
            order = val;
        }

        public static Level getLevel(int order) {
            return Arrays.stream(Level.values()).filter(level -> level.order == order).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("illegal order passed"));
        }
    }
}
