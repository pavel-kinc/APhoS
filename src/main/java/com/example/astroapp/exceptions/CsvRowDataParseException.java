package com.example.astroapp.exceptions;

public class CsvRowDataParseException extends CsvContentException {

    public CsvRowDataParseException() {
    }

    public CsvRowDataParseException(String message) {
        super(message);
    }

    public CsvRowDataParseException(Throwable cause) {
        super(cause);
    }

    public CsvRowDataParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
