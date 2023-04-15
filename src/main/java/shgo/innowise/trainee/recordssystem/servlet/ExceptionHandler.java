package shgo.innowise.trainee.recordssystem.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import shgo.innowise.trainee.recordssystem.exception.DefaultException;
import shgo.innowise.trainee.recordssystem.response.MessageResponse;
import shgo.innowise.trainee.recordssystem.response.ResponseEntity;

/**
 * Handles exceptions.
 */
@Slf4j
public class ExceptionHandler {

    private static volatile ExceptionHandler instance;

    private ExceptionHandler() {
    }

    /**
     * Gives instance of ExceptionHandler object.
     *
     * @return instance of ExceptionHandler
     */
    public static ExceptionHandler getInstance() {
        ExceptionHandler localInstance = instance;
        if (localInstance == null) {
            synchronized (ExceptionHandler.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = new ExceptionHandler();
                    localInstance = instance;
                }
            }
        }
        return localInstance;
    }

    /**
     * Puts exception's information to response entity.
     *
     * @param ex       exception
     * @return error message request as response entity
     */
    public ResponseEntity<MessageResponse> handleException(Exception ex) {
        MessageResponse objectToSend;
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

        return new ResponseEntity<>(objectToSend, status);
    }
}
