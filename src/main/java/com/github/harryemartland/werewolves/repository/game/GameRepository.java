package com.github.harryemartland.werewolves.repository.game;

import com.github.harryemartland.werewolves.domain.game.Game;

public interface GameRepository {

    Game getGame(String id) throws GameNotFoundException;

    Game getGameForAdmin(String sessionId) throws GameNotFoundException;

    Game getGameForPlayer(String sessionId) throws GameNotFoundException;

    void addGame(Game game);

}
