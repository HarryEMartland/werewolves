package com.github.harryemartland.werewolves.contoller;

import com.github.harryemartland.werewolves.repository.game.GameNotFoundException;
import com.github.harryemartland.werewolves.service.player.PlayerNotFoundException;
import com.github.harryemartland.werewolves.service.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @MessageMapping("/player/vote")
    public void vote(@Payload String votePlayer,
                           @Header("simpSessionId") String sessionId)
            throws PlayerNotFoundException, GameNotFoundException {
        playerService.vote(sessionId, votePlayer);
    }

}