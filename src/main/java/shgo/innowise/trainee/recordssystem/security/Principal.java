package shgo.innowise.trainee.recordssystem.security;

import lombok.Data;
import shgo.innowise.trainee.recordssystem.entity.Role;

import java.util.Set;

/**
 * Stores roles and username of user.
 */
@Data
public class Principal {
    private String username;
    private Set<Role> roles;
}
