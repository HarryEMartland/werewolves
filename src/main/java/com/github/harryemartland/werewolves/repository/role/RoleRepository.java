package com.github.harryemartland.werewolves.repository.role;

import com.github.harryemartland.werewolves.domain.role.Role;
import java.util.List;

public interface RoleRepository {

    Role findRole(String role) throws RoleNotFoundException;

    List<Role> getRoles();

}
