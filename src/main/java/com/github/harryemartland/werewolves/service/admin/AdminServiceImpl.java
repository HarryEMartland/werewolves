package com.github.harryemartland.werewolves.service.admin;

import com.github.harryemartland.werewolves.domain.game.Game;
import com.github.harryemartland.werewolves.domain.player.Player;
import com.github.harryemartland.werewolves.domain.role.Role;
import com.github.harryemartland.werewolves.dto.PlayerRole;
import com.github.harryemartland.werewolves.dto.RoleQuantity;
import com.github.harryemartland.werewolves.repository.game.GameNotFoundException;
import com.github.harryemartland.werewolves.repository.game.GameRepository;
import com.github.harryemartland.werewolves.repository.role.RoleNotFoundException;
import com.github.harryemartland.werewolves.repository.role.RoleRepository;
import com.github.harryemartland.werewolves.service.notification.NotificationService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private Random random;

    @Override
    public void clearVotes(String adminSessionId) throws GameNotFoundException {
        Game gameForAdmin = gameRepository.getGameForAdmin(adminSessionId)
                .orElseThrow(() -> new GameNotFoundException(adminSessionId));
        for (Player player : gameForAdmin.getPlayers()) {
            player.setVote(null);
            notificationService.playerVoted(gameForAdmin, player);
        }
    }

    @Override
    public List<PlayerRole> assignRoles(String adminSessionId, List<RoleQuantity> roleQuantities)
            throws GameNotFoundException, RoleNotFoundException {
        Game gameForAdmin = gameRepository.getGameForAdmin(adminSessionId)
                .orElseThrow(() -> new GameNotFoundException(adminSessionId));
        int numberOfPlayers = gameForAdmin.getPlayers().size();

        List<Role> mandatoryRoles = new ArrayList<>();
        List<Role> optionalRoles = new ArrayList<>();
        for (RoleQuantity roleQuantity : roleQuantities) {
            Role role = roleRepository.findRole(roleQuantity.getRoleName());
            for (int i = 0; i < roleQuantity.getMinimumPlayers(); i++) {
                mandatoryRoles.add(role);
            }
            for (int i = 0; i < roleQuantity.getRandomChancePlayers(); i++) {
                optionalRoles.add(role);
            }
        }

        Collections.shuffle(optionalRoles, random);
        List<Role> roles = fillWithOptionalRoles(numberOfPlayers, mandatoryRoles, optionalRoles);

        Collections.shuffle(roles, random);
        assignRolesToUsers(gameForAdmin, roles);

        return gameForAdmin.getPlayers().stream()
                .map(this::createPlayerRole)
                .collect(Collectors.toList());
    }

    private PlayerRole createPlayerRole(Player player) {
        PlayerRole playerRole = new PlayerRole();
        playerRole.setPlayer(player.getName());
        playerRole.setRole(player.getRole().getName());
        return playerRole;
    }

    private List<Role> fillWithOptionalRoles(
            int numberOfPlayers, List<Role> mandatoryRoles, List<Role> optionalRoles) {
        Iterator<Role> optionalRoleIterator = optionalRoles.iterator();
        List<Role> roles = new ArrayList<>(mandatoryRoles);
        while (roles.size() < numberOfPlayers) {
            roles.add(optionalRoleIterator.next());
        }
        return roles;
    }

    private void assignRolesToUsers(Game gameForAdmin, List<Role> roles) {
        Iterator<Role> roleIterator = roles.iterator();

        for (Player player : gameForAdmin.getPlayers()) {
            player.setRole(roleIterator.next());
            notificationService.roleAssigned(player);
        }
    }
}
