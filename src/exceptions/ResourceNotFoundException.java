package exceptions;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {

    private final UUID id;
    private final Class type;

    public ResourceNotFoundException(String msg, UUID id, Class type) {
        this.id = id;
        this.type = type;
    }
}
