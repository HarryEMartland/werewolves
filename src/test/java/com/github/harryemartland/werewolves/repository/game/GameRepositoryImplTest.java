package com.github.harryemartland.werewolves.repository.game;

import com.github.harryemartland.werewolves.domain.game.Game;
import com.github.harryemartland.werewolves.domain.player.Player;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import static com.github.harryemartland.werewolves.util.TestBuilder.mockGame;
import static com.github.harryemartland.werewolves.util.TestBuilder.mockPlayer;

public class GameRepositoryImplTest {

    private static final String ADMIN_SESSION_ID = "43k3k3";
    private static final String PLAYER_ID = "33j3";
    private GameRepository gameRepository = new GameRepositoryImpl();

    @Test
    public void shouldAddGame() throws GameNotFoundException {
        Game game = mockGame("1234");

        gameRepository.addGame(game);
        Assert.assertEquals(game, gameRepository.getGame("1234"));
    }

    @Test(expected = GameNotFoundException.class)
    public void shouldRemoveGame() throws GameNotFoundException {
        Game game = mockGame("1234");

        gameRepository.addGame(game);
        gameRepository.removeGame(game);
        gameRepository.getGame("1234");
    }

    @Test
    public void shouldGetGameForAdmin() throws GameNotFoundException {
        Player admin = Mockito.mock(Player.class);
        Mockito.when(admin.getSessionId()).thenReturn(ADMIN_SESSION_ID);

        Game game = Mockito.mock(Game.class);
        Mockito.when(game.getId()).thenReturn("1234");
        Mockito.when(game.getAdmin()).thenReturn(admin);

        gameRepository.addGame(game);
        Game adminGame = gameRepository.getGameForAdmin(ADMIN_SESSION_ID);
        Assert.assertEquals(game, adminGame);
    }

    @Test(expected = GameNotFoundException.class)
    public void shouldThrowExceptionWhenGameDoesNotExistForUser() throws GameNotFoundException {
        gameRepository.getGameForAdmin(ADMIN_SESSION_ID);
    }

    @Test
    public void shouldFindGameForPlayer() throws GameNotFoundException {
        Player player = mockPlayer(PLAYER_ID);
        Game game = mockGame(player);
        gameRepository.addGame(game);
        Game gameForPlayer = gameRepository.getGameForPlayer(PLAYER_ID);
        Assert.assertEquals(game, gameForPlayer);
    }

    @Test(expected = GameNotFoundException.class)
    public void shouldThrowExceptionWhenGameNotFoundForPlayer() throws GameNotFoundException {
        gameRepository.getGameForPlayer("not a player");
    }
}