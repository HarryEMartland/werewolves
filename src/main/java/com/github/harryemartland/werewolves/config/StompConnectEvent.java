package com.github.harryemartland.werewolves.config;

import com.github.harryemartland.werewolves.repository.game.GameNotFoundException;
import com.github.harryemartland.werewolves.service.game.GameService;
import com.github.harryemartland.werewolves.service.player.PlayerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class StompConnectEvent implements ApplicationListener<SessionDisconnectEvent> {

    @Autowired
    GameService gameService;

    /**
     * Calls game service leave game as the client has disconnected.
     *
     * @param event the disconnect event
     */
    public void onApplicationEvent(SessionDisconnectEvent event) {
        try {
            gameService.leaveGame(event.getSessionId());
        } catch (GameNotFoundException | PlayerNotFoundException e) {
            log.warn("Error Leaving Game", e);
        }
    }
}
