package shgo.innowise.trainee.recordssystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Set;

import lombok.*;
import shgo.innowise.trainee.recordssystem.entity.Role;

/**
 * Employee DTO.
 */
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String password;
    private Set<Role> roles;
}
