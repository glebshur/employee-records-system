package shgo.innowise.trainee.recordssystem.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import shgo.innowise.trainee.recordssystem.response.ResponseEntity;

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
     * @param responseEntity response entity to send
     * @param mapper json mapper
     */
    public static void fillJsonResponse(HttpServletResponse response,
                                        ResponseEntity<?> responseEntity,
                                        ObjectMapper mapper) {
        response.setStatus(responseEntity.getStatus());
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        try {
            String json = mapper.writeValueAsString(responseEntity.getBody());
            PrintWriter out = response.getWriter();
            out.println(json);
            out.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
