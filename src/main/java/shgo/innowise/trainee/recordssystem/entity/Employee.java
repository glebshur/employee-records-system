package shgo.innowise.trainee.recordssystem.entity;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

/**
 * Represents employee entity in database.
 */
@Data
public class Employee {
    private long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String password;
    private LocalDateTime creationDate;
    private Set<Role> roles;
}
