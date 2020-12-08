package com.thebluehats.server.game.enchants;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.EventTemplate;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

public class SprintDrainTest {

    @Test
    public void DamagerGetsSpeedWhenArrowHitsPlayer() {
        final int SPEED_DURATION = 3;
        final int SPEED_AMPLIFIER = 1;

        Player player = mock(Player.class);
        Player damagee = mock(Player.class);

        CustomEnchant<SprintDrainArgs> enchant = new SprintDrain(new EventTemplate[] { new ArrowHitPlayerTemplate() });

        enchant.execute(new SprintDrainArgs(player, damagee, SPEED_DURATION, SPEED_AMPLIFIER, 1));
        verify(player).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, SPEED_DURATION * 20, SPEED_AMPLIFIER));
    }

    @Test
    public void DamageeGetsSlownessWhenHitByArrow() {
        final int SPEED_DURATION = 3;
        final int SPEED_AMPLIFIER = 1;

        Player player = mock(Player.class);
        Player damagee = mock(Player.class);

        CustomEnchant<SprintDrainArgs> enchant = new SprintDrain(new EventTemplate[] { new ArrowHitPlayerTemplate() });

        enchant.execute(new SprintDrainArgs(player, damagee, SPEED_DURATION, SPEED_AMPLIFIER, 2));
        verify(damagee).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 0));
    }
}
