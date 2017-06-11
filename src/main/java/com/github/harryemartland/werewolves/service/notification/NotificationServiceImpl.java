package com.github.harryemartland.werewolves.service.notification;

import com.github.harryemartland.werewolves.domain.game.Game;
import com.github.harryemartland.werewolves.domain.player.Player;
import com.github.harryemartland.werewolves.dto.PlayerVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private SimpMessageSendingOperations messageSendingOperations;

    @Override
    public void playerJoinedGame(Game game, Player newPlayer) {
        sendToUser(game.getAdmin().getSessionId(), "/queue/player/joined", newPlayer.getName());
        for (Player player : game.getPlayers()) {
            if (player != newPlayer) {
                sendToUser(player.getSessionId(), "/queue/player/joined", newPlayer.getName());
            }
        }
    }

    @Override
    public void playerLeftGame(Game game, Player playerLeft) {
        sendToUser(game.getAdmin().getSessionId(), "/queue/player/left", playerLeft.getName());
        for (Player player : game.getPlayers()) {
            sendToUser(player.getSessionId(), "/queue/player/left", playerLeft.getName());
        }
    }

    @Override
    public void playerVoted(Game game, Player votedPlayer) {

        PlayerVote playerVote = new PlayerVote();
        playerVote.setSourcePlayer(votedPlayer.getName());
        playerVote.setTargetPlayer(votedPlayer.getVote().getName());

        sendToUser(game.getAdmin().getSessionId(), "/queue/player/voted", playerVote);
        for (Player player : game.getPlayers()) {
            sendToUser(player.getSessionId(), "/queue/player/voted", playerVote);
        }
    }

    @Override
    public void roleAssigned(Player player) {
        sendToUser(player.getSessionId(), "/queue/role/assigned", player.getRole());
    }

    private void sendToUser(String userId, String destination, Object data) {
        messageSendingOperations
                .convertAndSendToUser(userId, destination, data, createHeaders(userId));
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor =
                SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
