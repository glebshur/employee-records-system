package shgo.innowise.trainee.recordssystem.security;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import shgo.innowise.trainee.recordssystem.dao.EmployeeDAO;
import shgo.innowise.trainee.recordssystem.entity.Employee;
import shgo.innowise.trainee.recordssystem.exception.NotAuthorizedException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.StringTokenizer;

/**
 * Authentication service.
 */
@Slf4j
public class AuthenticationService {
    private PasswordEncoder passwordEncoder;
    private EmployeeDAO employeeDAO;
    @Getter
    private ThreadLocal<Principal> principal;

    private static AuthenticationService instance;

    private AuthenticationService() {
        passwordEncoder = PasswordEncoder.getInstance();
        employeeDAO = EmployeeDAO.getInstance();
    }

    /**
     * Gives instance of AuthenticationService object.
     *
     * @return instance of AuthenticationService
     */
    public static AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    /**
     * Authenticates user by his Basic credentials.
     *
     * @param authCredentials Basic credentials
     * @return true if user is authenticated; false if is not
     */
    public boolean authenticate(final String authCredentials) {

        if (null == authCredentials) {
            return false;
        }

        final String encodedUserAuthCredentials = authCredentials.replaceFirst("Basic ", "");
        byte[] decodedBytes = Base64.getDecoder().decode(encodedUserAuthCredentials);
        String decodedUserAuthCredentials = new String(decodedBytes, StandardCharsets.UTF_8);

        final StringTokenizer tokenizer = new StringTokenizer(decodedUserAuthCredentials, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();

        Employee employee = employeeDAO.get(username)
                .orElseThrow(() -> new NotAuthorizedException("Invalid username or password"));

        if (!passwordEncoder.getEncoder().matches(password, employee.getPassword())) {
            return false;
        }

        Principal currUser = new Principal();
        currUser.setUsername(employee.getEmail());
        currUser.setRoles(employee.getRoles());
        principal = ThreadLocal.withInitial(() -> currUser);

        return true;
    }

    /**
     * Clears LocalThread principal.
     */
    public void clearPrincipal() {
        principal = null;
    }
}
