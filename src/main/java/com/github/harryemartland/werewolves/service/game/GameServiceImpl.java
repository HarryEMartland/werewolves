package com.github.harryemartland.werewolves.service.game;

import com.github.harryemartland.werewolves.domain.game.Game;
import com.github.harryemartland.werewolves.domain.game.GameImpl;
import com.github.harryemartland.werewolves.domain.player.Player;
import com.github.harryemartland.werewolves.domain.player.PlayerImpl;
import com.github.harryemartland.werewolves.dto.GameRequest;
import com.github.harryemartland.werewolves.repository.game.GameNotFoundException;
import com.github.harryemartland.werewolves.repository.game.GameRepository;
import com.github.harryemartland.werewolves.service.notification.NotificationService;
import com.github.harryemartland.werewolves.service.player.PlayerNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public Game createGame(String sessionId, GameRequest gameRequest)
            throws UniqueIdException {

        checkIdIsUnique(gameRequest);

        PlayerImpl admin = new PlayerImpl(gameRequest.getName(), sessionId);
        GameImpl game = new GameImpl(gameRequest.getId(), admin);
        gameRepository.addGame(game);
        return game;
    }

    @Override
    public List<String> joinGame(String sessionId, GameRequest gameRequest)
            throws GameNotFoundException {
        Game game = gameRepository.getGame(gameRequest.getId());
        PlayerImpl newPlayer = new PlayerImpl(gameRequest.getName(), sessionId);
        game.addPlayer(newPlayer);
        notificationService.playerJoinedGame(game, newPlayer);
        return game.getPlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    @Override
    public void leaveGame(String sessionId) throws GameNotFoundException, PlayerNotFoundException {
        Game gameForPlayer = gameRepository.getGameForPlayer(sessionId);
        Player foundPlayer = gameForPlayer.getPlayers().stream()
                .filter(player -> player.getSessionId().equalsIgnoreCase(sessionId))
                .findFirst()
                .orElseThrow(PlayerNotFoundException::new);
        //todo if sessionId is for an admin delete the game and make clients reload
        // page so they have to connect to a new game
        gameForPlayer.removePlayer(foundPlayer);
        notificationService.playerLeftGame(gameForPlayer, foundPlayer);
    }

    private void checkIdIsUnique(GameRequest gameRequest) throws UniqueIdException {
        try {
            Game game = gameRepository.getGame(gameRequest.getId());
            if (game != null) {
                throw new UniqueIdException();
            }
        } catch (GameNotFoundException e) {
            log.trace("Game id not taken", e);
        }
    }
}
