package com.github.harryemartland.werewolves.domain.role;

import com.github.harryemartland.werewolves.domain.team.Team;
import com.github.harryemartland.werewolves.domain.team.VillagerTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LycanRole implements Role {

    @Autowired
    private VillagerTeam villagerTeam;

    @Override
    public String getName() {
        return "Lycan";
    }

    @Override
    public String getDescription() {
        return "You appear as a wolf to the seer";
    }

    @Override
    public Team getTeam() {
        return villagerTeam;
    }
}
