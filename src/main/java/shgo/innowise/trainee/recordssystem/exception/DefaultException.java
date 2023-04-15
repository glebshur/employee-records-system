package shgo.innowise.trainee.recordssystem.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Default exception with status.
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class DefaultException extends RuntimeException {
    private int status;

    public DefaultException(String message, int status) {
        super(message);
        this.status = status;
    }
}
