package com.example.astroapp.exceptions;

public class CsvContentException extends RuntimeException {

    public CsvContentException() {
        super();
    }

    public CsvContentException(String message) {
        super(message);
    }

    public CsvContentException(String message, Throwable cause) {
        super(message, cause);
    }
}
