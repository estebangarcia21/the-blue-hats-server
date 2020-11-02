package com.thebluehats.server.game.enchants;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

import com.thebluehats.server.game.managers.combat.BowManager;

public class WaspTest {
    @Test
    public void WeaknessIsAddedToPlayerWhenHitByArrow() {
        Player player = mock(Player.class);
        Wasp wasp = new Wasp(new BowManager());

        int duration = 2;
        int amplifier = 1;

        wasp.executeEnchant(player, duration, amplifier);

        verify(player).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, duration * 20, amplifier, true));
    }
}
