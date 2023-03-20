package shgo.innowise.trainee.recordssystem.exception;

import javax.servlet.http.HttpServletResponse;

/**
 * Save exception.
 */
public class SystemInternalException extends DefaultException {
    public SystemInternalException(String message) {
        super(message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    public SystemInternalException(String message, int status) {
        super(message, status);
    }
}
