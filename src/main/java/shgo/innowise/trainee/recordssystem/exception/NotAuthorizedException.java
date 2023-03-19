package shgo.innowise.trainee.recordssystem.exception;

import javax.servlet.http.HttpServletResponse;

/**
 * Not authorized exception.
 */
public class NotAuthorizedException extends DefaultException {
    public NotAuthorizedException(String message) {
        super(message, HttpServletResponse.SC_UNAUTHORIZED);
    }
}
