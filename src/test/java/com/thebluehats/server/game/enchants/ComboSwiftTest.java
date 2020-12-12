package com.thebluehats.server.game.enchants;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.thebluehats.server.game.enchants.args.common.PotionEffectWithHitsNeededArgs;
import com.thebluehats.server.game.managers.combat.templates.EventTemplate;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.HitCounter;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

public class ComboSwiftTest {

    @Test
    public void SpeedGivenIfHitsNeededAreReached() {
        final int HITS_NEEDED = 5;
        final int SPEED_TIME = 2;
        final int AMPLIFIER = 1;

        Player player = mock(Player.class);
        HitCounter hitCounter = mock(HitCounter.class);

        CustomEnchant<PotionEffectWithHitsNeededArgs> comboSwift = new ComboSwift(hitCounter,
                new EventTemplate[] { new PlayerHitPlayerTemplate() });

        PotionEffectWithHitsNeededArgs args = spy(
                new PotionEffectWithHitsNeededArgs(player, SPEED_TIME, AMPLIFIER, HITS_NEEDED));

        when(hitCounter.hasHits(player, HITS_NEEDED)).thenReturn(true);
        when(args.getPlayer()).thenReturn(player);

        comboSwift.execute(args);
        verify(player).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, SPEED_TIME * 20, AMPLIFIER, true));
    }

    @Test
    public void SpeedNotGivenIfHitsNeededAreNotReached() {
        final int HITS_NEEDED = 5;
        final int SPEED_TIME = 2;
        final int AMPLIFIER = 1;

        Player player = mock(Player.class);
        HitCounter hitCounter = mock(HitCounter.class);

        ComboSwift comboSwift = new ComboSwift(hitCounter, new EventTemplate[] { new PlayerHitPlayerTemplate() });

        PotionEffectWithHitsNeededArgs args = spy(
                new PotionEffectWithHitsNeededArgs(player, SPEED_TIME, AMPLIFIER, HITS_NEEDED));

        when(hitCounter.hasHits(player, HITS_NEEDED)).thenReturn(true);
        when(args.getPlayer()).thenReturn(player);

        comboSwift.execute(args);
        verify(player).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, SPEED_TIME * 20, AMPLIFIER, true));
    }
}
