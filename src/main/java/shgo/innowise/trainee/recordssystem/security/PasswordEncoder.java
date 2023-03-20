package shgo.innowise.trainee.recordssystem.security;

import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Contains bCrypt password encoder.
 */
@Getter
public class PasswordEncoder {
    private BCryptPasswordEncoder encoder;
    private static PasswordEncoder instance;

    private PasswordEncoder() {
        encoder = new BCryptPasswordEncoder();
    }

    /**
     * Gives instance of PasswordEncoder object.
     *
     * @return instance of PasswordEncoder
     */
    public static PasswordEncoder getInstance() {
        if (instance == null) {
            instance = new PasswordEncoder();
        }
        return instance;
    }
}
