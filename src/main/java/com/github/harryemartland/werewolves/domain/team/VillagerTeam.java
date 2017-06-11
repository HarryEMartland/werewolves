package com.github.harryemartland.werewolves.domain.team;

import org.springframework.stereotype.Component;

@Component
public class VillagerTeam implements Team {
    @Override
    public String getName() {
        return "Villager";
    }
}
