package me.stevemmmmm.server.game.enchants;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

public class SprintDrainTest {
    @Test
    public void testEnchantExecution() {
        SprintDrain enchant = new SprintDrain();
        Player player = mock(Player.class);
        Player damagee = mock(Player.class);

        int speedDuration = 3;
        int amplifier = 1;
        int level = 1;

        enchant.executeEnchant(player, damagee, speedDuration, amplifier, level);

        verify(player).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, speedDuration * 20, amplifier));

        level = 2;

        enchant.executeEnchant(player, damagee, speedDuration, amplifier, level);

        verify(damagee).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 0));
    }
}
