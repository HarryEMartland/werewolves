package com.github.harryemartland.werewolves.domain.role;

import com.github.harryemartland.werewolves.domain.team.Team;
import com.github.harryemartland.werewolves.domain.team.VillagerTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MasonRole implements Role {

    @Autowired
    private VillagerTeam villagerTeam;

    @Override
    public String getName() {
        return "Mason";
    }

    @Override
    public String getDescription() {
        return "You know who the other masons are";
    }

    @Override
    public Team getTeam() {
        return villagerTeam;
    }
}
