package shgo.innowise.trainee.recordssystem.security;

import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Contains bCrypt password encoder.
 */
@Getter
public class PasswordEncoder {
    private BCryptPasswordEncoder encoder;
    private static volatile PasswordEncoder instance;

    private PasswordEncoder() {
        encoder = new BCryptPasswordEncoder();
    }

    /**
     * Gives instance of PasswordEncoder object.
     *
     * @return instance of PasswordEncoder
     */
    public static PasswordEncoder getInstance() {
        PasswordEncoder localInstance = instance;
        if (localInstance == null) {
            synchronized (PasswordEncoder.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = new PasswordEncoder();
                    localInstance = instance;
                }
            }
        }
        return localInstance;
    }
}
