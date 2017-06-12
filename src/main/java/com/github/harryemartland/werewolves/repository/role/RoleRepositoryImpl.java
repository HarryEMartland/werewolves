package com.github.harryemartland.werewolves.repository.role;

import com.github.harryemartland.werewolves.domain.role.Role;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    @Autowired
    private List<Role> roles;

    @Override
    public Role findRole(String roleName) throws RoleNotFoundException {
        return roles.stream()
                .filter(role -> role.getName().equalsIgnoreCase(roleName))
                .findFirst()
                .orElseThrow(() -> new RoleNotFoundException(roleName));
    }

    @Override
    public List<Role> getRoles() {
        return roles;
    }
}
