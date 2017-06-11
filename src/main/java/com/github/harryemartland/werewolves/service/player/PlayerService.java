package com.github.harryemartland.werewolves.service.player;

import com.github.harryemartland.werewolves.repository.game.GameNotFoundException;

public interface PlayerService {

    void vote(String sessionId, String votePlayer)
            throws GameNotFoundException, PlayerNotFoundException;

}
