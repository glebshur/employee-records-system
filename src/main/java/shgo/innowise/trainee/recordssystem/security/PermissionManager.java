package shgo.innowise.trainee.recordssystem.security;

import shgo.innowise.trainee.recordssystem.entity.Role;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages users permissions.
 */
public class PermissionManager {
    // map of roles and paths available to them
    private Map<Role, List<String>> rolesPathsMap;

    private static volatile PermissionManager instance;

    private PermissionManager() {
        rolesPathsMap = new HashMap<>();

        for (Role role : Role.values()) {
            rolesPathsMap.put(role, new ArrayList<>());
        }
    }

    /**
     * Gives instance of PermissionManager object.
     *
     * @return instance of PermissionManager
     */
    public static PermissionManager getInstance() {
        PermissionManager localInstance = instance;
        if (localInstance == null) {
            synchronized (PermissionManager.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = new PermissionManager();
                    localInstance = instance;
                }
            }
        }
        return localInstance;
    }

    /**
     * Checks permission to path by principal.
     *
     * @param path      path to get permission
     * @param principal principal
     * @return true if principal has permission; false if hasn't
     */
    public boolean checkPermission(String path, Principal principal) {
        for (Role role : principal.getRoles()) {
            var match = rolesPathsMap.get(role).stream()
                    .filter(path::startsWith)
                    .findFirst();
            if (match.isPresent()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds path to role.
     *
     * @param role    role
     * @param newPath path to add
     */
    public void addPathToRole(Role role, String newPath) {
        List<String> paths = rolesPathsMap.get(role);
        if (paths == null) {
            throw new IllegalArgumentException("No such role in map: " + role.name());
        }
        paths.add(newPath);
    }
}
