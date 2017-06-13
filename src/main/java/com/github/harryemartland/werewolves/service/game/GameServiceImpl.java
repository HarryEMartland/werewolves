package com.github.harryemartland.werewolves.service.game;

import static com.github.harryemartland.werewolves.domain.GameStartType.CREATE;
import static com.github.harryemartland.werewolves.domain.GameStartType.JOIN;

import com.github.harryemartland.werewolves.domain.game.Game;
import com.github.harryemartland.werewolves.domain.game.GameImpl;
import com.github.harryemartland.werewolves.domain.player.Player;
import com.github.harryemartland.werewolves.domain.player.PlayerImpl;
import com.github.harryemartland.werewolves.dto.GameRequest;
import com.github.harryemartland.werewolves.repository.game.GameNotFoundException;
import com.github.harryemartland.werewolves.repository.game.GameRepository;
import com.github.harryemartland.werewolves.service.notification.NotificationService;
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
            throws UniqueGameIdException {

        checkIdIsUnique(gameRequest);

        PlayerImpl admin = new PlayerImpl(gameRequest.getName(), sessionId);
        GameImpl game = new GameImpl(gameRequest.getId(), admin);
        gameRepository.addGame(game);
        notificationService.gameStart(admin, CREATE);
        return game;
    }

    @Override
    public List<String> joinGame(String sessionId, GameRequest gameRequest)
            throws GameNotFoundException {
        Game game = gameRepository.getGame(gameRequest.getId());
        PlayerImpl newPlayer = new PlayerImpl(gameRequest.getName(), sessionId);
        game.addPlayer(newPlayer);
        notificationService.playerJoinedGame(game, newPlayer);
        notificationService.gameStart(newPlayer, JOIN);
        return game.getPlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    @Override
    public void leaveGame(String sessionId) throws GameNotFoundException {
        //todo remove try catches and make repo return optional
        try {
            Game gameForPlayer = gameRepository.getGameForPlayer(sessionId);

            gameForPlayer.getPlayers().stream()
                    .filter(player -> player.getSessionId().equalsIgnoreCase(sessionId))
                    .findFirst()
                    .ifPresent(player -> {
                        gameForPlayer.removePlayer(player);
                        notificationService.playerLeftGame(gameForPlayer, player);
                    });
        } catch (GameNotFoundException e) {
            log.trace("game not found for player {}", sessionId, e);
        }

        try {
            Game gameForAdmin = gameRepository.getGameForAdmin(sessionId);
            gameRepository.removeGame(gameForAdmin);
            notificationService.gameEnded(gameForAdmin);
        } catch (GameNotFoundException e) {
            log.trace("game not found for admin {}", sessionId, e);
        }

    }

    private void checkIdIsUnique(GameRequest gameRequest) throws UniqueGameIdException {
        try {
            Game game = gameRepository.getGame(gameRequest.getId());
            if (game != null) {
                throw new UniqueGameIdException(gameRequest.getId());
            }
        } catch (GameNotFoundException e) {
            log.trace("Game id not taken", e);
        }
    }
}
