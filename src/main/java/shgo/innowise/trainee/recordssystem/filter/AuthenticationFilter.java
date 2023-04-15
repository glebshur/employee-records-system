package shgo.innowise.trainee.recordssystem.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import shgo.innowise.trainee.recordssystem.controller.EmployeeController;
import shgo.innowise.trainee.recordssystem.exception.ForbiddenException;
import shgo.innowise.trainee.recordssystem.exception.NotAuthorizedException;
import shgo.innowise.trainee.recordssystem.response.MessageResponse;
import shgo.innowise.trainee.recordssystem.response.ResponseEntity;
import shgo.innowise.trainee.recordssystem.security.AuthenticationService;
import shgo.innowise.trainee.recordssystem.security.PermissionManager;
import shgo.innowise.trainee.recordssystem.servlet.ExceptionHandler;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static shgo.innowise.trainee.recordssystem.util.ControllerUtil.fillJsonResponse;

/**
 * Authentication filter.
 */
@Slf4j
public class AuthenticationFilter implements Filter {
    public static final String AUTHENTICATION_HEADER = "Authorization";
    private ObjectMapper objectMapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // need to initialize controllers
        EmployeeController.getInstance();

        objectMapper = new ObjectMapper();
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            try {
                final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
                final String authCredentials = httpServletRequest.getHeader(AUTHENTICATION_HEADER);

                AuthenticationService authenticationService = AuthenticationService.getInstance();
                final boolean authenticationStatus =
                        authenticationService.authenticate(authCredentials);

                if (authenticationStatus) {

                    if (!PermissionManager.getInstance().checkPermission(
                            httpServletRequest.getPathInfo(),
                            authenticationService.getPrincipal().get())) {
                        throw new ForbiddenException("Forbidden");
                    }

                    filterChain.doFilter(servletRequest, servletResponse);
                    authenticationService.clearPrincipal();
                } else {
                    if (servletResponse instanceof HttpServletResponse) {
                        throw new NotAuthorizedException("Invalid username or password");
                    }
                }
            } catch (Exception ex) {
                ResponseEntity<MessageResponse> responseEntity = ExceptionHandler.getInstance()
                        .handleException(ex);
                fillJsonResponse((HttpServletResponse) servletResponse,
                        responseEntity, objectMapper);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
