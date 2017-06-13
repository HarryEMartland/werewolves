package com.github.harryemartland.werewolves.domain.role;

import com.github.harryemartland.werewolves.domain.team.Team;
import com.github.harryemartland.werewolves.domain.team.VillagerTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DoctorRole implements Role {

    @Autowired
    private VillagerTeam villagerTeam;

    @Override
    public String getName() {
        return "Doctor";
    }

    @Override
    public String getDescription() {
        return "You can heal yourself or another player to protect from being killed by a wolf";
    }

    @Override
    public Team getTeam() {
        return villagerTeam;
    }
}
