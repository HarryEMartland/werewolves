package com.github.harryemartland.werewolves.domain.role;

import com.github.harryemartland.werewolves.domain.team.Team;
import com.github.harryemartland.werewolves.domain.team.WolvesTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MinionRole implements Role {

    @Autowired
    private WolvesTeam wolvesTeam;

    @Override
    public String getName() {
        return "Minion";
    }

    @Override
    public String getDescription() {
        return "You are to help the werewolves win";
    }

    @Override
    public Team getTeam() {
        return wolvesTeam;
    }
}
