package shgo.innowise.trainee.recordssystem.servlet;

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
     * @param response http servlet response
     */
    void handle(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
