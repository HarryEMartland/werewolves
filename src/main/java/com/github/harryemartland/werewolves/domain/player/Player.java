package com.github.harryemartland.werewolves.domain.player;

import com.github.harryemartland.werewolves.domain.role.Role;

public interface Player {

    String getName();

    void setRole(Role role);

    Role getRole();

    void setVote(Player vote);

    Player getVote();

    String getSessionId();

}
