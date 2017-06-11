package com.github.harryemartland.werewolves.service.admin;

import com.github.harryemartland.werewolves.dto.PlayerRole;
import com.github.harryemartland.werewolves.dto.RoleQuantity;
import com.github.harryemartland.werewolves.repository.game.GameNotFoundException;
import com.github.harryemartland.werewolves.repository.role.RoleNotFoundException;
import java.util.List;

public interface AdminService {

    void clearVotes(String adminSessionId) throws GameNotFoundException;

    List<PlayerRole> assignRoles(String adminSessionId, List<RoleQuantity> roleQuantities)
            throws GameNotFoundException, RoleNotFoundException;
}
