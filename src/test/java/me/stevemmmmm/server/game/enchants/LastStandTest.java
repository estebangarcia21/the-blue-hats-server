package me.stevemmmmm.server.game.enchants;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

public class LastStandTest {
    @Test
    public void testExecuteEnchant() {
        LastStand enchant = new LastStand();
        Player player = mock(Player.class);
        when(player.getHealth()).thenReturn(5D);

        int amplifier = 1;

        enchant.executeEnchant(player, amplifier);

        amplifier = 3;

        enchant.executeEnchant(player, amplifier);

        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80, amplifier, true));
    }
}
