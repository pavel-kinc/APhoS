package com.example.astroapp.exceptions;

import java.util.IllegalFormatException;

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
