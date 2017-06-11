package com.github.harryemartland.werewolves.service.player;

import static com.github.harryemartland.werewolves.util.TestBuilder.mockGame;
import static com.github.harryemartland.werewolves.util.TestBuilder.mockPlayer;
import com.github.harryemartland.werewolves.domain.game.Game;
import com.github.harryemartland.werewolves.domain.player.Player;
import com.github.harryemartland.werewolves.repository.game.GameNotFoundException;
import com.github.harryemartland.werewolves.repository.game.GameRepository;
import com.github.harryemartland.werewolves.service.notification.NotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceImplTest {

    private static final String GAME_ID = "3kkee";
    private static final String PLAYER_SESSION_ID = "kf3kj3";

    @Mock
    private GameRepository gameRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PlayerService playerService = new PlayerServiceImpl();

    @Test
    public void shouldVoteForPlayer() throws GameNotFoundException, PlayerNotFoundException {
        Player player = mockPlayer(PLAYER_SESSION_ID);
        Player player2 = mockPlayer("mrmkrkr", "player 2");
        Game game = mockGame(player, player2);
        Mockito.when(gameRepository.getGameForPlayer(PLAYER_SESSION_ID)).thenReturn(game);
        playerService.vote(PLAYER_SESSION_ID, "player 2");
        Mockito.verify(player).setVote(player2);
        Mockito.verify(notificationService).playerVoted(game, player);
    }
}