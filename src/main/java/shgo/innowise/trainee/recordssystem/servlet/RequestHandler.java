package shgo.innowise.trainee.recordssystem.servlet;

import shgo.innowise.trainee.recordssystem.response.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Functional interface for query handling in controllers.
 */
public interface RequestHandler {
    /**
     * Handles http request.
     *
     * @param request  http servlet request
     */
    ResponseEntity<?> handle(HttpServletRequest request) throws IOException;
}
