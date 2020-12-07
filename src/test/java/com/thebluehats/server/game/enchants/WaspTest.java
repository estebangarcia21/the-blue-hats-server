package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.enchants.args.PotionEffectArgs;
import com.thebluehats.server.game.managers.combat.BowManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayer;
import com.thebluehats.server.game.managers.combat.templates.EventTemplate;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WaspTest {
    @Test
    public void WeaknessIsAddedToPlayerWhenHitByArrow() {
        Player player = mock(Player.class);
        Wasp wasp = new Wasp(new BowManager(), new EventTemplate[] { new ArrowHitPlayer() });

        final int DURATION = 2;
        final int AMPLIFIER = 1;

        wasp.execute(new PotionEffectArgs(player, DURATION, AMPLIFIER));
        verify(player).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, DURATION * 20, AMPLIFIER, true));
    }
}
