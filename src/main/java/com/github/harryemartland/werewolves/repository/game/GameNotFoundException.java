package com.github.harryemartland.werewolves.repository.game;

import com.github.harryemartland.werewolves.domain.NotificationException;

public class GameNotFoundException extends NotificationException {

    public GameNotFoundException(String gameId) {
        super("error", "Game not found", "Could not find game for id " + gameId);
    }

}
