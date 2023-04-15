package shgo.innowise.trainee.recordssystem.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents an HTTP response, consisting of status code and body.
 *
 * @param <T> the body type
 */
@Getter
@AllArgsConstructor
public class ResponseEntity<T> {
    private final T body;
    private final int status;
}
