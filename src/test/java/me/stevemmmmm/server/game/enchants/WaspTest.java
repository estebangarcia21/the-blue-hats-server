package me.stevemmmmm.server.game.enchants;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

import me.stevemmmmm.server.game.managers.BowManager;

public class WaspTest {
    @Test
    public void testWeaknessIsAddedToPlayer() {
        Player player = mock(Player.class);
        Wasp wasp = new Wasp(new BowManager());

        wasp.executeEnchant(player, 1, 1);

        verify(player).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 1, true));
    }
}
