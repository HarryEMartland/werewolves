package com.github.harryemartland.werewolves.service.player;

import com.github.harryemartland.werewolves.domain.game.Game;
import com.github.harryemartland.werewolves.domain.player.Player;
import com.github.harryemartland.werewolves.repository.game.GameNotFoundException;
import com.github.harryemartland.werewolves.repository.game.GameRepository;
import com.github.harryemartland.werewolves.service.notification.NotificationService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void vote(String sessionId, String votePlayerName)
            throws GameNotFoundException, PlayerNotFoundException {

        Game gameForPlayer = gameRepository.getGameForPlayer(sessionId);

        Player currentUser = gameForPlayer.getPlayers().stream()
                .filter(player -> Objects.equals(player.getSessionId(), sessionId))
                .findFirst()
                .orElseThrow(PlayerNotFoundException::new);

        Player votePlayer = gameForPlayer.getPlayers().stream()
                .filter(player -> Objects.equals(player.getName(), votePlayerName))
                .findFirst()
                .orElseThrow(PlayerNotFoundException::new);

        currentUser.setVote(votePlayer);
        notificationService.playerVoted(gameForPlayer, currentUser);
    }


}
