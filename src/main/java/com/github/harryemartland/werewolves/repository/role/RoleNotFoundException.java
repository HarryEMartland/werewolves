package com.github.harryemartland.werewolves.repository.role;

import com.github.harryemartland.werewolves.domain.NotificationException;

public class RoleNotFoundException extends NotificationException {

    public RoleNotFoundException(String role) {
        super("error", "Role not Found", "Could not find role " + role);
    }

}
