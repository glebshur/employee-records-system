package shgo.innowise.trainee.recordssystem.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import shgo.innowise.trainee.recordssystem.exception.DefaultException;
import shgo.innowise.trainee.recordssystem.response.MessageResponse;
import shgo.innowise.trainee.recordssystem.util.ControllerUtil;

/**
 * Handles exceptions.
 */
@Slf4j
public class ExceptionHandler {
    private ObjectMapper mapper;

    private static ExceptionHandler instance;

    private ExceptionHandler() {
        mapper = new ObjectMapper();
    }

    /**
     * Gives instance of ExceptionHandler object.
     *
     * @return instance of ExceptionHandler
     */
    public static ExceptionHandler getInstance() {
        if (instance == null) {
            instance = new ExceptionHandler();
        }
        return instance;
    }

    /**
     * Puts exception's information to http response in json format.
     *
     * @param ex       exception
     * @param response http response
     */
    public void handleException(Exception ex, HttpServletResponse response) {
        Object objectToSend;
        int status;

        log.error(ex.getMessage());

        if (ex instanceof NumberFormatException) {
            status = HttpServletResponse.SC_BAD_REQUEST;
            objectToSend = new MessageResponse("Bad Request");

        } else if (ex instanceof IOException) {
            status = HttpServletResponse.SC_BAD_REQUEST;
            objectToSend = new MessageResponse("Bad Request");

        } else if (ex instanceof DefaultException) {
            DefaultException defaultException = (DefaultException) ex;
            status = defaultException.getStatus();
            objectToSend = new MessageResponse(defaultException.getMessage());

        } else {
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            objectToSend = new MessageResponse("Internal error");
        }

        ControllerUtil.fillJsonResponse(response, status, mapper, objectToSend);
    }
}
