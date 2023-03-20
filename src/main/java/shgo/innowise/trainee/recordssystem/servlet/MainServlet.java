package shgo.innowise.trainee.recordssystem.servlet;


import lombok.extern.slf4j.Slf4j;
import shgo.innowise.trainee.recordssystem.controller.EmployeeController;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Servlet accepts all requests.
 */
@WebServlet(name = "MainServlet", urlPatterns = "/*")
@Slf4j
public class MainServlet extends HttpServlet {

    private ExceptionHandler exceptionHandler;
    private RoutingHelper helper;

    @Override
    public void init() throws ServletException {
        // need to initialize controllers
        EmployeeController.getInstance();

        exceptionHandler = ExceptionHandler.getInstance();
        helper = RoutingHelper.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            RequestHandler handler = helper.findGetHandler(req.getPathInfo());
            handler.handle(req, resp);
        } catch (Exception ex) {
            exceptionHandler.handleException(ex, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            RequestHandler handler = helper.findPostHandler(req.getPathInfo());
            handler.handle(req, resp);
        } catch (Exception ex) {
            exceptionHandler.handleException(ex, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            RequestHandler handler = helper.findPuttHandler(req.getPathInfo());
            handler.handle(req, resp);
        } catch (Exception ex) {
            exceptionHandler.handleException(ex, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            RequestHandler handler = helper.findDeleteHandler(req.getPathInfo());
            handler.handle(req, resp);
        } catch (Exception ex) {
            exceptionHandler.handleException(ex, resp);
        }
    }
}
