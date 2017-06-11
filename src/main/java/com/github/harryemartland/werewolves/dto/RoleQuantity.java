package com.github.harryemartland.werewolves.dto;

import lombok.Data;

@Data
public class RoleQuantity {

    private String roleName;
    private int minimumPlayers;
    private int randomChancePlayers;

}
