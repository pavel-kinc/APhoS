package cz.muni.aphos.exceptions;


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
