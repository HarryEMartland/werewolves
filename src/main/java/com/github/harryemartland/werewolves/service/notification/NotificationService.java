package com.github.harryemartland.werewolves.service.notification;

import com.github.harryemartland.werewolves.domain.game.Game;
import com.github.harryemartland.werewolves.domain.player.Player;

public interface NotificationService {

    void playerJoinedGame(Game game, Player player);

    void playerLeftGame(Game game, Player player);

    void playerVoted(Game game, Player player);

    void roleAssigned(Player player);

}
