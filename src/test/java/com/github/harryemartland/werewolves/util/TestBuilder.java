package com.github.harryemartland.werewolves.util;

import com.github.harryemartland.werewolves.domain.game.Game;
import com.github.harryemartland.werewolves.domain.player.Player;
import com.github.harryemartland.werewolves.domain.role.Role;
import java.util.Arrays;
import org.mockito.Mockito;

public class TestBuilder {

    public static Game mockGame(String id){
        Game game = Mockito.mock(Game.class);
        Mockito.when(game.getId()).thenReturn(id);
        return game;
    }

    public static Game mockGame(Player...players){
        Game game = Mockito.mock(Game.class);
        Mockito.when(game.getPlayers()).thenReturn(Arrays.asList(players));
        return game;
    }

    public static Game mockGameWithAdmin(Player admin, Player...players){
        Game game = Mockito.mock(Game.class);
        Mockito.when(game.getPlayers()).thenReturn(Arrays.asList(players));
        Mockito.when(game.getAdmin()).thenReturn(admin);
        return game;
    }

    public static Player mockPlayer(String sessionId){
        Player player = Mockito.mock(Player.class);
        Mockito.when(player.getSessionId()).thenReturn(sessionId);
        return player;
    }

    public static Player mockPlayer(String sessionId, String name){
        Player player = Mockito.mock(Player.class);
        Mockito.when(player.getSessionId()).thenReturn(sessionId);
        Mockito.when(player.getName()).thenReturn(name);
        Role role = mockRole("mock role");
        Mockito.when(player.getRole()).thenReturn(role);
        return player;
    }

    public static Role mockRole(){
        return Mockito.mock(Role.class);
    }

    public static Role mockRole(String name){
        Role role = Mockito.mock(Role.class);
        Mockito.when(role.getName()).thenReturn(name);
        return role;
    }
}
