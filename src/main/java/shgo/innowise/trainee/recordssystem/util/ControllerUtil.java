package shgo.innowise.trainee.recordssystem.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Contains information and methods for generating an http response.
 */
@Slf4j
public class ControllerUtil {
    public static final String CONTENT_TYPE = "application/json";
    public static final String CHARACTER_ENCODING = "UTF-8";

    /**
     * Fills response with json data.
     *
     * @param response response to send
     * @param status response status
     * @param mapper json mapper
     * @param objectToSend object to send
     */
    public static void fillJsonResponse(HttpServletResponse response,
                                  int status,
                                  ObjectMapper mapper,
                                  Object objectToSend) {
        response.setStatus(status);
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        try {
            String json = mapper.writeValueAsString(objectToSend);
            PrintWriter out = response.getWriter();
            out.println(json);
            out.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
