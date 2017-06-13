package com.github.harryemartland.werewolves.repository.game;

import com.github.harryemartland.werewolves.domain.game.Game;
import java.util.Optional;

public interface GameRepository {

    Game getGame(String id) throws GameNotFoundException;

    Optional<Game> getGameForAdmin(String sessionId);

    Optional<Game> getGameForPlayer(String sessionId);

    void addGame(Game game);

    void removeGame(Game game);
}
