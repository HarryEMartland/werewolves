package com.github.harryemartland.werewolves.service.player;

import com.github.harryemartland.werewolves.domain.NotificationException;

public class PlayerNotFoundException extends NotificationException {
    public PlayerNotFoundException() {
        super("error", "Player not found", "Could not find player");
    }
}
