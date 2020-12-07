package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.enchants.args.PotionEffectArgs;
import com.thebluehats.server.game.enchants.args.PotionEffectWithHitsNeededArgs;
import com.thebluehats.server.game.managers.combat.templates.EventTemplate;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayer;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.HitCounter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ComboSwiftTest {

    @Test
    public void SpeedGivenIfHitsNeededAreReached() {
        final int HITS_NEEDED = 5;
        final int SPEED_TIME = 2;
        final int AMPLIFIER = 1;

        Player player = mock(Player.class);
        HitCounter hitCounter = mock(HitCounter.class);

        CustomEnchant<PotionEffectWithHitsNeededArgs> comboSwift = new ComboSwift(hitCounter, new EventTemplate[] { new PlayerHitPlayer() });

        PotionEffectWithHitsNeededArgs args = spy(new PotionEffectWithHitsNeededArgs(player, SPEED_TIME, AMPLIFIER, HITS_NEEDED));

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

        ComboSwift comboSwift = new ComboSwift(hitCounter, new EventTemplate[] { new PlayerHitPlayer() });

        PotionEffectWithHitsNeededArgs args = spy(new PotionEffectWithHitsNeededArgs(player, SPEED_TIME, AMPLIFIER, HITS_NEEDED));

        when(hitCounter.hasHits(player, HITS_NEEDED)).thenReturn(true);
        when(args.getPlayer()).thenReturn(player);

        comboSwift.execute(args);
        verify(player).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, SPEED_TIME * 20, AMPLIFIER, true));
    }
}
