package com.github.harryemartland.werewolves.dto;

import lombok.Data;

@Data
public class PlayerVote {

    private String sourcePlayer;
    private String targetPlayer;
}
