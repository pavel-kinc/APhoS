package cz.muni.aphos.exceptions;

/**
 * The Exception for the unexpected format of a specific csv row.
 */
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
