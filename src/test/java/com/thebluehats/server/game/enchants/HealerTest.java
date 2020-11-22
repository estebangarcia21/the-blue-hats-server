package com.thebluehats.server.game.enchants;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class HealerTest {
    @Test
    public void DamagerAndDamageeGetHealedWhenHit() {
        Healer enchant = new Healer();
        Player damaged = mock(Player.class);
        Player damager = mock(Player.class);

        double maxHealth = 20;
        int healAmount = 4;

        when(damaged.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(mock(AttributeInstance.class));
        when(damager.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(mock(AttributeInstance.class));

        when(damaged.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()).thenReturn(maxHealth);
        when(damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()).thenReturn(maxHealth);

        enchant.execute(new HealerArgs(damager, damaged, healAmount));

        verify(damaged).setHealth(damaged.getHealth() + healAmount);
        verify(damager).setHealth(damager.getHealth() + healAmount);

        enchant.execute(new HealerArgs(damager, damaged, 22));

        verify(damaged).setHealth(maxHealth);
        verify(damager).setHealth(maxHealth);
    }
}
