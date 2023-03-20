package shgo.innowise.trainee.recordssystem.exception;

import javax.servlet.http.HttpServletResponse;

/**
 * Forbidden exception.
 */
public class ForbiddenException extends DefaultException {
    public ForbiddenException(String message) {
        super(message, HttpServletResponse.SC_FORBIDDEN);
    }
}
