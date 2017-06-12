package com.github.harryemartland.werewolves.domain.role;

import com.github.harryemartland.werewolves.WerewolvesApplication;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

@RunWith(Parameterized.class)
@ContextConfiguration(classes = WerewolvesApplication.class)
public class RoleTest {

    private static List<Role> roles;

    @Parameterized.Parameters
    public static List<Object[]> setup() {
        TestContextManager testContextManager = new TestContextManager(RoleTest.class);

        Map<String, Role> roleMap = testContextManager
                .getTestContext()
                .getApplicationContext()
                .getBeansOfType(Role.class);

        roles = roleMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        return roles.stream()
                .map(role -> new Object[]{role})
                .collect(Collectors.toList());
    }

    @Parameterized.Parameter
    public Role role;

    @Test
    public void shouldHaveUniqueName() {
        Assert.assertEquals(1, roles.stream()
                .filter(role1 -> role1.getName().equalsIgnoreCase(role.getName()))
                .count());
    }

    @Test
    public void shouldHaveName() {
        Assert.assertTrue(role.getName().length() > 0);
    }

    @Test
    public void shouldHaveDescription() {
        Assert.assertTrue(role.getDescription().length() > 0);
    }

    @Test
    public void shouldHaveTeam() {
        Assert.assertNotNull(role.getTeam());
    }
}