package com.github.harryemartland.werewolves.domain.game;

import com.github.harryemartland.werewolves.domain.player.Player;
import java.util.ArrayList;
import java.util.List;

public class GameImpl implements Game {

    private String id;
    private Player admin;
    private List<Player> players = new ArrayList<>();

    public GameImpl(String id, Player admin) {
        this.id = id;
        this.admin = admin;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void addPlayer(Player player) {
        players.add(player);
    }

    @Override
    public void removePlayer(Player player) {
        players.remove(player);
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public Player getAdmin() {
        return admin;
    }
}
