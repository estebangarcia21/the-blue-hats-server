package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.enchants.args.PotionEffectArgs;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayer;
import com.thebluehats.server.game.managers.combat.templates.EventTemplate;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PeroxideTest {
    @Test
    public void DamageeGetsRegenerationWhenHit() {
        Peroxide enchant = new Peroxide(new EventTemplate[] { new ArrowHitPlayer(), new PlayerHitPlayer() });
        Player player = mock(Player.class);

        int duration = 2;
        int amplifier = 1;

        enchant.execute(new PotionEffectArgs(player, duration, amplifier));

        verify(player).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration * 20, amplifier));
    }
}
