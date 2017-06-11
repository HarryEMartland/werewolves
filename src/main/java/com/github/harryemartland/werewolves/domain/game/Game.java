package com.github.harryemartland.werewolves.domain.game;

import com.github.harryemartland.werewolves.domain.player.Player;
import java.util.List;

public interface Game {

    String getId();

    void addPlayer(Player player);

    void removePlayer(Player player);

    List<Player> getPlayers();

    Player getAdmin();

}
