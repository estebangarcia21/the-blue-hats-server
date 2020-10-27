package me.stevemmmmm.server.game.enchants;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

public class ComboSwiftTest {
    @Test
    public void testSpeedGivenOnHit() {
        Player player = mock(Player.class);
        HitCounter hitCounter = mock(HitCounter.class);
        ComboSwift comboSwift = new ComboSwift(hitCounter);

        when(hitCounter.hasHits(player, 5)).thenReturn(true);

        comboSwift.executeEnchant(player, 5, 1, 1);

        verify(player).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1, true));
    }
}
