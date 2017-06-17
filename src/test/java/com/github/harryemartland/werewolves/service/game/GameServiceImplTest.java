package com.github.harryemartland.werewolves.service.game;

import static com.github.harryemartland.werewolves.util.TestBuilder.mockGame;
import static com.github.harryemartland.werewolves.util.TestBuilder.mockPlayer;

import com.github.harryemartland.werewolves.domain.GameStartType;
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
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceImplTest {

    private static final String SESSION_ID = "k3k3j";
    private static final String PLAYER_1 = "user1";
    private static final String PLAYER_2 = "user2";
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
        Mockito.verify(notificationService).gameStart(Mockito.any(), Mockito.eq(GameStartType.CREATE));
    }

    @Test
    public void shouldJoinGame() throws GameNotFoundException, DuplicatePlayerException {
        GameRequest gameRequest = new GameRequest();
        gameRequest.setName(PLAYER_2);
        gameRequest.setId(GAME_ID);

        Player player = mockPlayer(SESSION_ID, PLAYER_1);
        Game mockGame = mockGame(player);

        Mockito.when(gameRepository.getGame(GAME_ID)).thenReturn(mockGame);

        List<String> users = gameService.joinGame(SESSION_ID, gameRequest);
        Assert.assertEquals(Collections.singletonList(PLAYER_1), users);

        PlayerImpl expectedNewPlayer = new PlayerImpl(PLAYER_2, SESSION_ID);
        Mockito.verify(mockGame).addPlayer(Mockito.eq(expectedNewPlayer));
        Mockito.verify(notificationService).playerJoinedGame(Mockito.same(mockGame), Mockito.eq(expectedNewPlayer));
        Mockito.verify(notificationService).gameStart(expectedNewPlayer, GameStartType.JOIN);
    }

    @Test
    public void shouldLeaveGamePlayer() throws GameNotFoundException, PlayerNotFoundException {
        Player player = mockPlayer(SESSION_ID);
        Game game = mockGame(player);
        Mockito.when(gameRepository.getGameForPlayer(SESSION_ID)).thenReturn(Optional.of(game));
        Mockito.when(gameRepository.getGameForAdmin(SESSION_ID)).thenReturn(Optional.empty());

       gameService.leaveGame(SESSION_ID);

       Mockito.verify(game).removePlayer(player);
       Mockito.verify(notificationService).playerLeftGame(game, player);
    }

    @Test
    public void shouldLeaveGameAdmin() throws GameNotFoundException {
        Player player = mockPlayer(SESSION_ID);
        Game game = mockGame(player);
        Mockito.when(gameRepository.getGameForPlayer(SESSION_ID)).thenReturn(Optional.empty());
        Mockito.when(gameRepository.getGameForAdmin(SESSION_ID)).thenReturn(Optional.of(game));
        gameService.leaveGame(SESSION_ID);
        Mockito.verify(gameRepository).removeGame(game);
        Mockito.verify(notificationService).gameEnded(game);
    }

    @Test(expected = UniqueGameIdException.class)
    public void shouldThrowExceptionIfGameIdAlreadyInUse() throws GameNotFoundException, UniqueGameIdException {

        Game game = mockGame(GAME_ID);

        Mockito.when(gameRepository.getGame(GAME_ID)).thenReturn(game);

        GameRequest gameRequest = new GameRequest();
        gameRequest.setId(GAME_ID);

        gameService.createGame(SESSION_ID, gameRequest);
    }

    @Test(expected = DuplicatePlayerException.class)
    public void shouldThrowExceptionWhenPlayerNameAlreadyExists() throws GameNotFoundException, DuplicatePlayerException {
        Player player = mockPlayer(SESSION_ID, PLAYER_1);

        Game game = mockGame(player);

        Mockito.when(gameRepository.getGame(GAME_ID)).thenReturn(game);

        GameRequest gameRequest = new GameRequest();
        gameRequest.setId(GAME_ID);
        gameRequest.setName(PLAYER_1);
        gameService.joinGame("d3dd3", gameRequest);
    }
}