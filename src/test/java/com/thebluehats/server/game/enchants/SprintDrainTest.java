package com.thebluehats.server.game.enchants;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

public class SprintDrainTest {
    private int SPEED_DURATION = 3;
    private int SPEED_AMPLIFIER = 1;

    @Test
    public void DamagerGetsSpeedWhenArrowHitsPlayer() {
        SprintDrain enchant = new SprintDrain();
        Player player = mock(Player.class);
        Player damagee = mock(Player.class);

        enchant.execute(new SprintDrainArgs(player, damagee, SPEED_DURATION, SPEED_AMPLIFIER, 1));
        verify(player).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, SPEED_DURATION * 20, SPEED_AMPLIFIER));

        enchant.execute(new SprintDrainArgs(player, damagee, SPEED_DURATION, SPEED_AMPLIFIER, 2));
        verify(damagee).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 0));
    }

    @Test
    public void DamageeGetsSlownessWhenHitByArrow() {
        SprintDrain enchant = new SprintDrain();
        Player player = mock(Player.class);
        Player damagee = mock(Player.class);

        enchant.execute(new SprintDrainArgs(player, damagee, SPEED_DURATION, SPEED_AMPLIFIER, 2));
        verify(damagee).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 0));
    }
}
