package shgo.innowise.trainee.recordssystem.exception;

import javax.servlet.http.HttpServletResponse;

/**
 * Not found exception.
 */
public class NotFoundException extends DefaultException {
    public NotFoundException(String message) {
        super(message, HttpServletResponse.SC_NOT_FOUND);
    }

    public NotFoundException(String message, int status) {
        super(message, status);
    }
}
