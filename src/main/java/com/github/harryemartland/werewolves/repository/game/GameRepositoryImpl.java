package com.github.harryemartland.werewolves.repository.game;

import com.github.harryemartland.werewolves.domain.game.Game;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
    public Optional<Game> getGameForAdmin(String sessionId) {
        return gameMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(game -> game.getAdmin().getSessionId().equalsIgnoreCase(sessionId))
                .findFirst();
    }

    @Override
    public Optional<Game> getGameForPlayer(String sessionId) {
        return gameMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(game -> game.getPlayers().stream()
                        .anyMatch(player -> player.getSessionId().equalsIgnoreCase(sessionId)))
                .findFirst();
    }

    @Override
    public void addGame(Game game) {
        gameMap.put(game.getId(), game);
    }

    @Override
    public void removeGame(Game game) {
        gameMap.remove(game.getId());
    }
}
