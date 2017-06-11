package com.github.harryemartland.werewolves.domain.player;

import com.github.harryemartland.werewolves.domain.role.Role;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class PlayerImpl implements Player {

    private String name;
    private Role role;
    private Player vote;
    private String sessionId;

    public PlayerImpl(String name, String sessionId) {
        this.name = name;
        this.sessionId = sessionId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public void setVote(Player vote) {
        this.vote = vote;
    }

    @Override
    public Player getVote() {
        return vote;
    }

    public String getSessionId() {
        return sessionId;
    }
}
