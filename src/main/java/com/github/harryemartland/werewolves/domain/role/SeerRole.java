package com.github.harryemartland.werewolves.domain.role;

import com.github.harryemartland.werewolves.domain.team.Team;
import com.github.harryemartland.werewolves.domain.team.VillagerTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SeerRole implements Role {

    @Autowired
    private VillagerTeam villagerTeam;

    @Override
    public String getName() {
        return "Seer";
    }

    @Override
    public String getDescription() {
        return "Wakes up at night to 'see' if another player is a werewolf";
    }

    @Override
    public Team getTeam() {
        return villagerTeam;
    }
}
