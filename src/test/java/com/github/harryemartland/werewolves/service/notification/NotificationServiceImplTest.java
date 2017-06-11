package com.github.harryemartland.werewolves.service.notification;

import com.github.harryemartland.werewolves.domain.game.Game;
import com.github.harryemartland.werewolves.domain.player.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import static com.github.harryemartland.werewolves.util.TestBuilder.mockGameWithAdmin;
import static com.github.harryemartland.werewolves.util.TestBuilder.mockPlayer;
import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceImplTest {

    @Mock
    private SimpMessageSendingOperations messageSendingOperations;

    @InjectMocks
    private NotificationService notificationService = new NotificationServiceImpl();

    @Test
    public void shouldNotSendJoinNotificationToJoiningUser() {

        Player admin = mockPlayer("1", "admin");
        Player player2 = mockPlayer("2", "player2");
        Player player3 = mockPlayer("3", "player3");


        Game game = mockGameWithAdmin(admin, player2,player3);

        notificationService.playerJoinedGame(game, player3);

        verifyNewUserEvent("1", "player3",1);
        verifyNewUserEvent("2", "player3",1);
        verifyNewUserEvent("3", "player3",0);
    }

    private void verifyNewUserEvent(String sessionId, String player, int times) {
        Mockito.verify(messageSendingOperations, Mockito.times(times))
                .convertAndSendToUser(eq(sessionId), eq("/queue/player/joined"), eq(player), Mockito.anyMap());
    }
}