package com.github.harryemartland.werewolves.domain.role;

import com.github.harryemartland.werewolves.domain.team.Team;
import com.github.harryemartland.werewolves.domain.team.WolvesTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WerewolfRole implements Role {

    @Autowired
    private WolvesTeam wolvesTeam;

    @Override
    public String getName() {
        return "Werewolf";
    }

    @Override
    public String getDescription() {
        return "kills someone every night";
    }

    @Override
    public Team getTeam() {
        return wolvesTeam;
    }
}
