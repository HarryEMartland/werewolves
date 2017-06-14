package com.github.harryemartland.werewolves.service.notification;

import static com.github.harryemartland.werewolves.util.TestBuilder.mockGameWithAdmin;
import static com.github.harryemartland.werewolves.util.TestBuilder.mockPlayer;
import static com.github.harryemartland.werewolves.util.TestBuilder.mockRole;
import static org.mockito.Matchers.eq;

import com.github.harryemartland.werewolves.domain.GameStartType;
import com.github.harryemartland.werewolves.domain.game.Game;
import com.github.harryemartland.werewolves.domain.player.Player;
import com.github.harryemartland.werewolves.domain.role.Role;
import com.github.harryemartland.werewolves.dto.PlayerVote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceImplTest {

    private static final VerificationMode ONE_TIME = Mockito.times(1);

    @Mock
    private SimpMessageSendingOperations messageSendingOperations;

    @InjectMocks
    private NotificationService notificationService = new NotificationServiceImpl();

    private Player admin = mockPlayer("1", "admin");
    private Player player2 = mockPlayer("2", "player2");
    private Player player3 = mockPlayer("3", "player3");

    private Game game = mockGameWithAdmin(admin, player2, player3);

    @Test
    public void shouldNotSendJoinNotificationToJoiningUser() {
        notificationService.playerJoinedGame(game, player3);

        verityEventSentToAllPlayersExcept("/queue/player/joined", "player3", player3, admin, player2);
    }

    @Test
    public void shouldNotSendNotificationToVotingUser() {

        Mockito.when(player3.getVote()).thenReturn(player2);

        notificationService.playerVoted(game, player3);

        PlayerVote playerVote = new PlayerVote();
        playerVote.setTargetPlayer("player2");
        playerVote.setSourcePlayer("player3");

        verityEventSentToAllPlayersExcept("/queue/player/voted", playerVote, player3, admin, player2);
    }

    @Test
    public void shouldNotifyRoleAssigned() {
        Role role = mockRole("test role");
        Mockito.when(player2.getRole()).thenReturn(role);
        notificationService.roleAssigned(player2);
        verifyEventSent("2", "/queue/role/assigned", role);
    }

    @Test
    public void shouldNotifyOnGameStart() {
        notificationService.gameStart(player2, GameStartType.CREATE);
        verifyEventSent("2", "/queue/game/start", GameStartType.CREATE);
    }

    @Test
    public void shouldNotifyOnGameEnd() {
        notificationService.gameEnded(game);
        verityEventSentToAllPlayersExcept("/queue/game/ended", true, null, admin, player2, player3);
    }

    @Test
    public void shouldNotifyOnPlayerLeaving() {
        notificationService.playerLeftGame(game, player2);
        verityEventSentToAllPlayersExcept("/queue/player/left", "player2", null, admin, player2, player3);
    }

    private void verityEventSentToAllPlayersExcept(String path, Object payload, Player exept, Player... players) {
        for (Player player : players) {
            if (player != exept) {
                verifyEventSent(player.getSessionId(), path, payload);
            }
        }
    }

    private void verifyEventSent(String sessionId, String path, Object payload) {
        Mockito.verify(messageSendingOperations, ONE_TIME).convertAndSendToUser(
                eq(sessionId),
                eq(path),
                eq(payload),
                Mockito.anyMapOf(String.class, Object.class)
        );
    }
}