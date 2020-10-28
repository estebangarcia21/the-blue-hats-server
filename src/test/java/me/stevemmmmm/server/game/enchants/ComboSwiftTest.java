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

        int hitsNeeded = 5;
        int speedTime = 2;
        int amplifier = 1;

        when(hitCounter.hasHits(player, hitsNeeded)).thenReturn(true);

        comboSwift.executeEnchant(player, hitsNeeded, speedTime, amplifier);

        verify(player).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, speedTime * 20, 1, true));
    }
}
