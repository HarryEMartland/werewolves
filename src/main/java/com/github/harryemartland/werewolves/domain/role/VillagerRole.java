package com.github.harryemartland.werewolves.domain.role;

import com.github.harryemartland.werewolves.domain.team.Team;
import com.github.harryemartland.werewolves.domain.team.VillagerTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VillagerRole implements Role {

    @Autowired
    private VillagerTeam villagerTeam;

    @Override
    public String getName() {
        return "Villager";
    }

    @Override
    public String getDescription() {
        return "You are just a boring villager, you have no powers";
    }

    @Override
    public Team getTeam() {
        return villagerTeam;
    }
}
