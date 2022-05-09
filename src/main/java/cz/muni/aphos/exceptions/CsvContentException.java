package cz.muni.aphos.exceptions;


/**
 * The Exception for the bad format of the csv files, e.g. missing schema, missing initial info...
 */
public class CsvContentException extends RuntimeException {

    public CsvContentException() {
        super();
    }

    public CsvContentException(Throwable cause) {
        super(cause);
    }

    public CsvContentException(String message) {
        super(message);
    }

    public CsvContentException(String message, Throwable cause) {
        super(message, cause);
    }
}
