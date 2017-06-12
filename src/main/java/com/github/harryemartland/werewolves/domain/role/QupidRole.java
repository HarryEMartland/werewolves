package com.github.harryemartland.werewolves.domain.role;

import com.github.harryemartland.werewolves.domain.team.Team;
import com.github.harryemartland.werewolves.domain.team.VillagerTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QupidRole implements Role {

    @Autowired
    private VillagerTeam villagerTeam;

    @Override
    public String getName() {
        return "Qupid";
    }

    @Override
    public String getDescription() {
        return "You make two players fall in love, theses players see each others role";
    }

    @Override
    public Team getTeam() {
        return villagerTeam;
    }
}
