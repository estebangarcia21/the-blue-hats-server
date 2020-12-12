package com.thebluehats.server.game.enchants;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.thebluehats.server.game.enchants.args.common.PotionEffectArgs;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.EventTemplate;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

public class PeroxideTest {
    @Test
    public void DamageeGetsRegenerationWhenHit() {
        Peroxide enchant = new Peroxide(
                new EventTemplate[] { new ArrowHitPlayerTemplate(), new PlayerHitPlayerTemplate() });
        Player player = mock(Player.class);

        int duration = 2;
        int amplifier = 1;

        enchant.execute(new PotionEffectArgs(player, duration, amplifier));

        verify(player).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration * 20, amplifier));
    }
}
