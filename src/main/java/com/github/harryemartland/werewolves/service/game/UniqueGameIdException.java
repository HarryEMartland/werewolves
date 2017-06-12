package com.github.harryemartland.werewolves.service.game;

import com.github.harryemartland.werewolves.domain.NotificationException;

public class UniqueGameIdException extends NotificationException {

    public UniqueGameIdException(String id) {
        super("error","Unique Game Id", "Game already exists for " + id);
    }
}
