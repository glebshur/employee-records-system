package shgo.innowise.trainee.recordssystem.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import shgo.innowise.trainee.recordssystem.controller.EmployeeController;
import shgo.innowise.trainee.recordssystem.response.ResponseEntity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static shgo.innowise.trainee.recordssystem.util.ControllerUtil.fillJsonResponse;


/**
 * Servlet accepts all requests.
 */
@WebServlet(name = "MainServlet", urlPatterns = "/*")
@Slf4j
public class MainServlet extends HttpServlet {

    private ExceptionHandler exceptionHandler;
    private RoutingHelper helper;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        // need to initialize controllers
        EmployeeController.getInstance();

        exceptionHandler = ExceptionHandler.getInstance();
        helper = RoutingHelper.getInstance();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ResponseEntity<?> responseEntity;
        try {
            RequestHandler handler = helper.findGetHandler(req.getPathInfo());
            responseEntity = handler.handle(req);
        } catch (Exception ex) {
            responseEntity = exceptionHandler.handleException(ex);
        }
        fillJsonResponse(resp, responseEntity, objectMapper);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ResponseEntity<?> responseEntity;
        try {
            RequestHandler handler = helper.findPostHandler(req.getPathInfo());
            responseEntity = handler.handle(req);
        } catch (Exception ex) {
            responseEntity = exceptionHandler.handleException(ex);
        }
        fillJsonResponse(resp, responseEntity, objectMapper);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ResponseEntity<?> responseEntity;
        try {
            RequestHandler handler = helper.findPuttHandler(req.getPathInfo());
            responseEntity = handler.handle(req);
        } catch (Exception ex) {
            responseEntity = exceptionHandler.handleException(ex);
        }
        fillJsonResponse(resp, responseEntity, objectMapper);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ResponseEntity<?> responseEntity;
        try {
            RequestHandler handler = helper.findDeleteHandler(req.getPathInfo());
            responseEntity = handler.handle(req);
        } catch (Exception ex) {
            responseEntity = exceptionHandler.handleException(ex);
        }
        fillJsonResponse(resp, responseEntity, objectMapper);
    }
}
