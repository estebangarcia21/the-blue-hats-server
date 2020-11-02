package com.thebluehats.server.game.enchants;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class LastStandTest {
    @Test
    public void GivesResistanceWhenHealthIsLessThan10() {
        LastStand enchant = new LastStand();
        Player player = mock(Player.class);
        when(player.getHealth()).thenReturn(5D);

        int amplifier = 1;

        enchant.executeEnchant(player, amplifier);

        verify(player).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80, amplifier, true));
    }

    @Test
    public void DoesNotGiveResistanceWhenHealthIsGreaterThan10() {
        LastStand enchant = new LastStand();
        Player player = mock(Player.class);
        when(player.getHealth()).thenReturn(15D);

        int amplifier = 1;

        enchant.executeEnchant(player, amplifier);

        verify(player, never()).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80, amplifier, true));
    }
}
