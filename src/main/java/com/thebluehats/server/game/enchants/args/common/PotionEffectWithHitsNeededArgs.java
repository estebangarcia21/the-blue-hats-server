package com.thebluehats.server.game.enchants.args;

import org.bukkit.entity.Player;

public class PotionEffectWithHitsNeededArgs extends PotionEffectArgs {
    private int hitsNeeded;

    public PotionEffectWithHitsNeededArgs(Player player, int duration, int amplifier, int hitsNeeded) {
        super(player, duration, amplifier);

        this.hitsNeeded = hitsNeeded;
    }

    public int getHitsNeeded() {
        return hitsNeeded;
    }
}

