package com.github.harryemartland.werewolves.contoller;

import com.github.harryemartland.werewolves.dto.GameRequest;
import com.github.harryemartland.werewolves.repository.game.GameNotFoundException;
import com.github.harryemartland.werewolves.service.game.DuplicatePlayerException;
import com.github.harryemartland.werewolves.service.game.GameService;
import com.github.harryemartland.werewolves.service.game.UniqueGameIdException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @MessageMapping("/game/create")
    public void createGame(@Payload GameRequest gameRequest,
                           @Header("simpSessionId") String sessionId) throws UniqueGameIdException {
        gameService.createGame(sessionId, gameRequest);
    }

    @MessageMapping("/game/join")
    @SendToUser
    public List<String> joinGame(@Payload GameRequest gameRequest,
                                 @Header("simpSessionId") String sessionId)
            throws GameNotFoundException, DuplicatePlayerException {
        return gameService.joinGame(sessionId, gameRequest);
    }

}