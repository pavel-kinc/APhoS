package cz.muni.var4astro.exceptions;

import java.util.IllegalFormatException;


/**
 * The Exception for the incorrectly formatted coordinate input.
 */
public class IllegalCoordinateFormatException extends RuntimeException {

    public IllegalCoordinateFormatException() {
    }

    public IllegalCoordinateFormatException(String message) {
        super(message);
    }

    public IllegalCoordinateFormatException(Throwable cause) {
        super(cause);
    }
}
