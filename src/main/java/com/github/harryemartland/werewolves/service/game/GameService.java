package com.github.harryemartland.werewolves.service.game;

import com.github.harryemartland.werewolves.domain.game.Game;
import com.github.harryemartland.werewolves.dto.GameRequest;
import com.github.harryemartland.werewolves.repository.game.GameNotFoundException;
import com.github.harryemartland.werewolves.service.player.PlayerNotFoundException;
import java.util.List;

public interface GameService {

    Game createGame(String sessionId, GameRequest gameRequest) throws UniqueGameIdException;

    List<String> joinGame(String sessionId, GameRequest gameRequest) throws GameNotFoundException;

    void leaveGame(String sessionId) throws GameNotFoundException, PlayerNotFoundException;

}
