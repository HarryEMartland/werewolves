package com.github.harryemartland.werewolves.contoller;

import com.github.harryemartland.werewolves.domain.role.Role;
import com.github.harryemartland.werewolves.repository.role.RoleRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    List<Role> getRoles() {
        return roleRepository.getRoles();
    }

}
