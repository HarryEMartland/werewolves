package com.github.harryemartland.werewolves.domain.role;

import com.github.harryemartland.werewolves.domain.team.Team;

public interface Role {

    String getName();

    String getDescription();

    Team getTeam();

}
