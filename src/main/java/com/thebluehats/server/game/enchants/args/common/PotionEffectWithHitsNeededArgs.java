package com.thebluehats.server.game.enchants.args.common;

import org.bukkit.entity.Player;

public class PotionEffectWithHitsNeededArgs extends PotionEffectArgs {
    private final int hitsNeeded;

    public PotionEffectWithHitsNeededArgs(Player player, int duration, int amplifier, int hitsNeeded) {
        super(player, duration, amplifier);

        this.hitsNeeded = hitsNeeded;
    }

    public int getHitsNeeded() {
        return hitsNeeded;
    }
}
