package com.github.harryemartland.werewolves.service.game;

import com.github.harryemartland.werewolves.domain.game.Game;
import com.github.harryemartland.werewolves.domain.player.Player;
import com.github.harryemartland.werewolves.domain.player.PlayerImpl;
import com.github.harryemartland.werewolves.dto.GameRequest;
import com.github.harryemartland.werewolves.repository.game.GameNotFoundException;
import com.github.harryemartland.werewolves.repository.game.GameRepository;
import com.github.harryemartland.werewolves.service.notification.NotificationService;
import com.github.harryemartland.werewolves.service.player.PlayerNotFoundException;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static com.github.harryemartland.werewolves.util.TestBuilder.mockGame;
import static com.github.harryemartland.werewolves.util.TestBuilder.mockPlayer;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceImplTest {

    private static final String SESSION_ID = "k3k3j";
    private static final String USER_1 = "user1";
    private static final String GAME_ID = "1234";

    @Mock
    private GameRepository gameRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    public void shouldCreateGame() throws UniqueGameIdException {

        GameRequest gameRequest = new GameRequest();
        gameRequest.setName("user1");
        gameRequest.setId("1234");

        Game game = gameService.createGame(SESSION_ID, gameRequest);
        Assert.assertEquals("1234", game.getId());
        Assert.assertEquals("user1", game.getAdmin().getName());
        Assert.assertEquals("k3k3j", game.getAdmin().getSessionId());

        Mockito.verify(gameRepository).addGame(game);
    }

    @Test
    public void shouldJoinGame() throws GameNotFoundException {
        GameRequest gameRequest = new GameRequest();
        gameRequest.setName(USER_1);
        gameRequest.setId(GAME_ID);

        Player player = mockPlayer(SESSION_ID, USER_1);
        Game mockGame = mockGame(player);

        Mockito.when(gameRepository.getGame(GAME_ID)).thenReturn(mockGame);

        List<String> users = gameService.joinGame(SESSION_ID, gameRequest);
        Assert.assertEquals(Collections.singletonList(USER_1), users);

        PlayerImpl expectedNewPlayer = new PlayerImpl(USER_1, SESSION_ID);
        Mockito.verify(mockGame).addPlayer(Mockito.eq(expectedNewPlayer));
        Mockito.verify(notificationService).playerJoinedGame(Mockito.same(mockGame), Mockito.eq(expectedNewPlayer));
    }

    @Test
    public void shouldLeaveGamePlayer() throws GameNotFoundException, PlayerNotFoundException {
        Player player = mockPlayer(SESSION_ID);
        Game game = mockGame(player);
        Mockito.when(gameRepository.getGameForPlayer(SESSION_ID)).thenReturn(game);
        Mockito.when(gameRepository.getGameForAdmin(SESSION_ID)).thenThrow(Mockito.mock(GameNotFoundException.class));

       gameService.leaveGame(SESSION_ID);

       Mockito.verify(game).removePlayer(player);
       Mockito.verify(notificationService).playerLeftGame(game, player);
    }

    @Test
    public void shouldLeaveGameAdmin() throws GameNotFoundException {
        Player player = mockPlayer(SESSION_ID);
        Game game = mockGame(player);
        Mockito.when(gameRepository.getGameForPlayer(SESSION_ID)).thenThrow(Mockito.mock(GameNotFoundException.class));
        Mockito.when(gameRepository.getGameForAdmin(SESSION_ID)).thenReturn(game);
        gameService.leaveGame(SESSION_ID);
        Mockito.verify(gameRepository).removeGame(game);
    }

    @Test(expected = UniqueGameIdException.class)
    public void shouldThrowExceptionIfGameIdAlreadyInUse() throws GameNotFoundException, UniqueGameIdException {

        Game game = mockGame(GAME_ID);

        Mockito.when(gameRepository.getGame(GAME_ID)).thenReturn(game);

        GameRequest gameRequest = new GameRequest();
        gameRequest.setId(GAME_ID);

        gameService.createGame(SESSION_ID, gameRequest);
    }
}