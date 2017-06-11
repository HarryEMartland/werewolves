package com.github.harryemartland.werewolves.contoller;

import com.github.harryemartland.werewolves.dto.PlayerRole;
import com.github.harryemartland.werewolves.dto.RoleQuantityList;
import com.github.harryemartland.werewolves.repository.game.GameNotFoundException;
import com.github.harryemartland.werewolves.repository.role.RoleNotFoundException;
import com.github.harryemartland.werewolves.service.admin.AdminService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @MessageMapping("/admin/role/assign")
    @SendToUser
    public List<PlayerRole> assignRoles(@Header("simpSessionId") String sessionId,
                                        @Payload RoleQuantityList roleQuantities)
            throws GameNotFoundException, RoleNotFoundException {
        return adminService.assignRoles(sessionId, roleQuantities);
    }

    @MessageMapping("/admin/votes/clear")
    public void clearVotes(@Header("simpSessionId") String sessionId)
            throws GameNotFoundException {
        adminService.clearVotes(sessionId);
    }
}