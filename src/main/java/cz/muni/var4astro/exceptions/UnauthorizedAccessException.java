package cz.muni.var4astro.exceptions;


/**
 * The Exception thrown when accessing secured endpoint without authentication.
 */
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
    }

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
