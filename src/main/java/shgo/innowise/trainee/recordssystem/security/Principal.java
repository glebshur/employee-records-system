package shgo.innowise.trainee.recordssystem.security;

import lombok.*;
import shgo.innowise.trainee.recordssystem.entity.Role;

import java.util.Set;

/**
 * Stores roles and username of user.
 */
@NoArgsConstructor
@Setter
@Getter
public class Principal {
    private String username;
    private Set<Role> roles;
}
