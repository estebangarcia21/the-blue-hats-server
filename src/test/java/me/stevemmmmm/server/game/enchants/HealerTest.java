package me.stevemmmmm.server.game.enchants;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.junit.Test;

public class HealerTest {
    @Test
    public void testPlayersGetHealed() {
        Healer enchant = new Healer();
        Player damaged = mock(Player.class);
        Player damager = mock(Player.class);

        when(damaged.getHealth()).thenReturn(0D);
        when(damager.getHealth()).thenReturn(0D);

        when(damaged.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(mock(AttributeInstance.class));
        when(damager.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(mock(AttributeInstance.class));

        when(damaged.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()).thenReturn(20D);
        when(damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()).thenReturn(20D);

        enchant.executeEnchant(damager, damaged, 3);

        verify(damaged).setHealth(damaged.getHealth() + 3);
        verify(damager).setHealth(damager.getHealth() + 3);
    }
}
