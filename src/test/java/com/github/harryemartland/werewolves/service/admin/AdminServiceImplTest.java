package com.github.harryemartland.werewolves.service.admin;

import com.github.harryemartland.werewolves.domain.game.Game;
import com.github.harryemartland.werewolves.domain.player.Player;
import com.github.harryemartland.werewolves.domain.role.Role;
import com.github.harryemartland.werewolves.dto.RoleQuantity;
import com.github.harryemartland.werewolves.dto.PlayerRole;
import com.github.harryemartland.werewolves.repository.game.GameNotFoundException;
import com.github.harryemartland.werewolves.repository.game.GameRepository;
import com.github.harryemartland.werewolves.repository.role.RoleNotFoundException;
import com.github.harryemartland.werewolves.repository.role.RoleRepository;
import com.github.harryemartland.werewolves.service.notification.NotificationService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import static com.github.harryemartland.werewolves.util.TestBuilder.mockGame;
import static com.github.harryemartland.werewolves.util.TestBuilder.mockPlayer;
import static com.github.harryemartland.werewolves.util.TestBuilder.mockRole;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceImplTest {

    private static final String ADMIN_SESSION_ID = "34243";

    @Mock
    private GameRepository gameRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private NotificationService notificationService;

    @Spy
    private Random random = new Random(0);

    @InjectMocks
    private AdminService adminService = new AdminServiceImpl();

    private Player player1 = mockPlayer("Fdaf", "player1");
    private Player player2 = mockPlayer("trf4", "player2");
    private Player player3 = mockPlayer("2caf", "player3");

    private Role role1 = mockRole("role1");
    private Role role2 = mockRole("role2");
    private Role role3 = mockRole("role3");
    private Role role4 = mockRole("role4");
    private Role role5 = mockRole("role5");

    private Game game = mockGame(player1, player2, player3);

    @Before
    public void setUp() throws RoleNotFoundException, GameNotFoundException {
        Mockito.when(gameRepository.getGameForAdmin(ADMIN_SESSION_ID)).thenReturn(game);
        Mockito.when(roleRepository.findRole("role1")).thenReturn(role1);
        Mockito.when(roleRepository.findRole("role2")).thenReturn(role2);
        Mockito.when(roleRepository.findRole("role3")).thenReturn(role3);
        Mockito.when(roleRepository.findRole("role4")).thenReturn(role4);
        Mockito.when(roleRepository.findRole("role5")).thenReturn(role5);
    }

    @Test
    public void shouldClearAllVotes() throws GameNotFoundException {

        adminService.clearVotes(ADMIN_SESSION_ID);

        Mockito.verify(player1).setVote(null);
        Mockito.verify(notificationService).playerVoted(game, player1);
        Mockito.verify(player2).setVote(null);
        Mockito.verify(notificationService).playerVoted(game, player2);
        Mockito.verify(player3).setVote(null);
        Mockito.verify(notificationService).playerVoted(game, player3);
    }

    @Test
    public void shouldAssignRolesAllOneRole() throws GameNotFoundException, RoleNotFoundException {

        adminService.assignRoles(ADMIN_SESSION_ID, Collections.singletonList(
                createRoleQuantity("role1", 3, 0)));

        Mockito.verify(player1).setRole(role1);
        Mockito.verify(notificationService).roleAssigned(player1);
        Mockito.verify(player2).setRole(role1);
        Mockito.verify(notificationService).roleAssigned(player2);
        Mockito.verify(player3).setRole(role1);
        Mockito.verify(notificationService).roleAssigned(player3);
    }

    @Test
    public void shouldAssignRoleTwoMandatory() throws RoleNotFoundException, GameNotFoundException {
        List<PlayerRole> playerRoles = adminService.assignRoles(ADMIN_SESSION_ID, Arrays.asList(
                createRoleQuantity("role1", 1, 0),
                createRoleQuantity("role2", 2, 0)
        ));

        Mockito.verify(player1).setRole(role2);
        Mockito.verify(notificationService).roleAssigned(player1);
        Mockito.verify(player2).setRole(role2);
        Mockito.verify(notificationService).roleAssigned(player2);
        Mockito.verify(player3).setRole(role1);
        Mockito.verify(notificationService).roleAssigned(player3);

        Assert.assertEquals(Arrays.asList(
                createPlayerRole("player1", "mock role"),
                createPlayerRole("player2", "mock role"),
                createPlayerRole("player3", "mock role")
        ), playerRoles);
    }

    @Test
    public void shouldAssignRoleTwoMandatoryOneOptional() throws RoleNotFoundException, GameNotFoundException {
        List<PlayerRole> playerRoles = adminService.assignRoles(ADMIN_SESSION_ID, Arrays.asList(
                createRoleQuantity("role1", 1, 0),
                createRoleQuantity("role2", 1, 0),
                createRoleQuantity("role3", 0, 1)
        ));

        Mockito.verify(player1).setRole(role3);
        Mockito.verify(notificationService).roleAssigned(player1);
        Mockito.verify(player2).setRole(role2);
        Mockito.verify(notificationService).roleAssigned(player2);
        Mockito.verify(player3).setRole(role1);
        Mockito.verify(notificationService).roleAssigned(player3);

        Assert.assertEquals(Arrays.asList(
                createPlayerRole("player1", "mock role"),
                createPlayerRole("player2", "mock role"),
                createPlayerRole("player3", "mock role")
        ), playerRoles);
    }

    @Test
    public void shouldAssignRoleTwoMandatoryTowOptional() throws RoleNotFoundException, GameNotFoundException {
        List<PlayerRole> playerRoles = adminService.assignRoles(ADMIN_SESSION_ID, Arrays.asList(
                createRoleQuantity("role1", 1, 0),
                createRoleQuantity("role2", 1, 0),
                createRoleQuantity("role3", 0, 1),
                createRoleQuantity("role4", 0, 1)
        ));

        Mockito.verify(player1).setRole(role3);
        Mockito.verify(notificationService).roleAssigned(player1);
        Mockito.verify(player2).setRole(role1);
        Mockito.verify(notificationService).roleAssigned(player2);
        Mockito.verify(player3).setRole(role2);
        Mockito.verify(notificationService).roleAssigned(player3);

        Assert.assertEquals(Arrays.asList(
                createPlayerRole("player1", "mock role"),
                createPlayerRole("player2", "mock role"),
                createPlayerRole("player3", "mock role")
        ), playerRoles);
    }

    private RoleQuantity createRoleQuantity(String roleName, int minimumPlayers, int randomChancePlayers) {
        RoleQuantity roleQuantity = new RoleQuantity();
        roleQuantity.setMinimumPlayers(minimumPlayers);
        roleQuantity.setRandomChancePlayers(randomChancePlayers);
        roleQuantity.setRoleName(roleName);
        return roleQuantity;
    }

    private PlayerRole createPlayerRole(String user, String role){
        PlayerRole playerRole = new PlayerRole();
        playerRole.setPlayer(user);
        playerRole.setRole(role);
        return playerRole;
    }

}