package com.github.harryemartland.werewolves.repository.game;

import com.github.harryemartland.werewolves.domain.game.Game;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private Map<String, Game> gameMap = new HashMap<>();

    @Override
    public Game getGame(String id) throws GameNotFoundException {
        Game game = gameMap.get(id);
        if (game == null) {
            throw new GameNotFoundException(id);
        }
        return game;
    }

    @Override
    public Game getGameForAdmin(String sessionId) throws GameNotFoundException {
        return gameMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(game -> game.getAdmin().getSessionId().equalsIgnoreCase(sessionId))
                .findFirst()
                .orElseThrow(() -> new GameNotFoundException(sessionId));
    }

    @Override
    public Game getGameForPlayer(String sessionId) throws GameNotFoundException {
        return gameMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(game -> game.getPlayers().stream()
                        .anyMatch(player -> player.getSessionId().equalsIgnoreCase(sessionId)))
                .findFirst()
                .orElseThrow(() -> new GameNotFoundException(sessionId));
    }

    @Override
    public void addGame(Game game) {
        gameMap.put(game.getId(), game);
    }
}
