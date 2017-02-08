package ua.nure.kozina.SummaryTask4.exception;

/**
 * The class of exceptions occurs while working with database.
 *
 * @author V. Kozina-Kravchenko
 */
public class DBException extends ApplicationException {

    /**
     * Constructs a new database exception with the specified error message.
     *
     * @param message an error message
     */
    public DBException(String message) {
        super(message);
    }

    /**
     * Constructs a new database exception with the specified error message and
     * caused exception.
     *
     * @param message an error message
     * @param cause a caused exception object
     */
    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new database exception with the specified caused exception.
     *
     * @param cause a caused exception
     */
    public DBException(Throwable cause) {
        super(cause);
    }
}
