package me.stevemmmmm.server.game.enchants;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

public class PeroxideTest {
    @Test
    public void testRegenerationIsGivenToPlayer() {
        Peroxide enchant = new Peroxide();
        Player player = mock(Player.class);

        enchant.executeEnchant(player, 1, 1);

        verify(player).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 1));
    }
}
