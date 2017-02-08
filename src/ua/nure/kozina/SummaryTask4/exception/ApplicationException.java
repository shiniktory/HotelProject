package ua.nure.kozina.SummaryTask4.exception;

/**
 * The class of application exceptions.
 *
 * @author V. Kozina-Kravchenko
 */
public class ApplicationException extends Exception {

    /**
     * Constructs a new application exception with the specified error message.
     *
     * @param message an error message
     */
    public ApplicationException(String message) {
        super(message);
    }

    /**
     * Constructs a new application exception with the specified error message and
     * caused exception.
     *
     * @param message an error message
     * @param cause a caused exception object
     */
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new application exception with the specified caused exception.
     *
     * @param cause a caused exception
     */
    public ApplicationException(Throwable cause) {
        super(cause);
    }
}
