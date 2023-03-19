package shgo.innowise.trainee.recordssystem.servlet;


import static java.util.Map.entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import shgo.innowise.trainee.recordssystem.controller.EmployeeController;
import shgo.innowise.trainee.recordssystem.exception.NotFoundException;


/**
 * Servlet accepts all requests.
 */
@WebServlet(name = "MainServlet", urlPatterns = "/*")
@Slf4j
public class MainServlet extends HttpServlet {

    private Map<String, RequestHandler> getRequestHandlerMap;
    private Map<String, RequestHandler> postRequestHandlerMap;
    private Map<String, RequestHandler> putRequestHandlerMap;
    private Map<String, RequestHandler> deleteRequestHandlerMap;
    private ExceptionHandler exceptionHandler;

    @Override
    public void init() throws ServletException {
        exceptionHandler = ExceptionHandler.getInstance();
        EmployeeController employeeController = EmployeeController.getInstance();

        getRequestHandlerMap = Map.ofEntries(
                entry(EmployeeController.GET_ALL_EMPLOYEES_PATH,
                        employeeController::getAllEmployees),
                entry(EmployeeController.GET_EMPLOYEE_PATH,
                        employeeController::getEmployee));

        postRequestHandlerMap = Map.ofEntries(
                entry(EmployeeController.CREATE_EMPLOYEE_PATH,
                        employeeController::createEmployee));

        putRequestHandlerMap = Map.ofEntries(
                entry(EmployeeController.UPDATE_EMPLOYEE_PATH,
                        employeeController::updateEmployee));

        deleteRequestHandlerMap = Map.ofEntries(
                entry(EmployeeController.DELETE_EMPLOYEE_PATH,
                        employeeController::deleteEmployee));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            RequestHandler handler = getHandler(getRequestHandlerMap, req.getPathInfo());
            handler.handle(req, resp);
        } catch (Exception ex) {
            exceptionHandler.handleException(ex, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            RequestHandler handler = getHandler(postRequestHandlerMap, req.getPathInfo());
            handler.handle(req, resp);
        } catch (Exception ex) {
            exceptionHandler.handleException(ex, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            RequestHandler handler = getHandler(putRequestHandlerMap, req.getPathInfo());
            handler.handle(req, resp);
        } catch (Exception ex) {
            exceptionHandler.handleException(ex, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            RequestHandler handler = getHandler(deleteRequestHandlerMap, req.getPathInfo());
            handler.handle(req, resp);
        } catch (Exception ex) {
            exceptionHandler.handleException(ex, resp);
        }
    }

    /**
     * Finds handler in request handler map by path.
     *
     * @param requestHandlerMap map with paths and handlers
     * @param path              request path
     * @return request handler
     */
    private RequestHandler getHandler(Map<String, RequestHandler> requestHandlerMap, String path) {
        var match = requestHandlerMap.entrySet().stream()
                .filter(entry -> path.startsWith(entry.getKey()))
                .findFirst();

        return match.map(Map.Entry::getValue).orElseThrow(() -> new NotFoundException("Not Found"));
    }
}
