package com.github.harryemartland.werewolves.repository.role;

import static com.github.harryemartland.werewolves.util.TestBuilder.mockRole;
import com.github.harryemartland.werewolves.domain.role.Role;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryImplTest {

    @InjectMocks
    private RoleRepository roleRepository = new RoleRepositoryImpl();

    private Role role1 = mockRole("role1");
    private Role role2 = mockRole("role2");
    private Role role3 = mockRole("role3");

    private List<Role> roles = Arrays.asList(role1, role2, role3);

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(roleRepository, "roles", roles);
    }

    @Test
    public void shouldFindRole() throws RoleNotFoundException {
        Role result = roleRepository.findRole("role1");
        Assert.assertEquals(role1, result);
    }

    @Test(expected = RoleNotFoundException.class)
    public void shouldThrowExceptionWhenRoleNotFound() throws RoleNotFoundException {
        roleRepository.findRole("not a role");
    }
}