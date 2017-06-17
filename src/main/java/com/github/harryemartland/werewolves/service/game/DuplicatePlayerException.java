package com.github.harryemartland.werewolves.service.game;

import com.github.harryemartland.werewolves.domain.NotificationException;

public class DuplicatePlayerException extends NotificationException {
    /**
     * Exception to create a formatted notification in the ui.
     *
     * @param playerName    the player name trying to be created
     */
    public DuplicatePlayerException(String playerName) {
        super("error", "Duplicate User", "A user already exists called " + playerName);
    }
}
