package com.github.harryemartland.werewolves.domain.role;

import com.github.harryemartland.werewolves.domain.team.Team;
import com.github.harryemartland.werewolves.domain.team.VillagerTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HunterRole implements Role {

    @Autowired
    private VillagerTeam villagerTeam;

    @Override
    public String getName() {
        return "Hunter";
    }

    @Override
    public String getDescription() {
        return "Once per game you can kill a player";
    }

    @Override
    public Team getTeam() {
        return villagerTeam;
    }
}
