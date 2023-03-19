package shgo.innowise.trainee.recordssystem.exception;

import javax.servlet.http.HttpServletResponse;

/**
 * Employee creation exception.
 */
public class SaveException extends DefaultException {
    public SaveException(String message) {
        super(message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    public SaveException(String message, int status) {
        super(message, status);
    }
}
