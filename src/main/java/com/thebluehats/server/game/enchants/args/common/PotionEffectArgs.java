package com.thebluehats.server.game.enchants.args.common;

import org.bukkit.entity.Player;

public class PotionEffectArgs {
    private final Player player;
    private final int duration;
    private final int amplifier;

    public PotionEffectArgs(Player player, int duration, int amplifier) {
        this.player = player;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public Player getPlayer() {
        return player;
    }

    public int getDuration() {
        return duration;
    }

    public int getAmplifier() {
        return amplifier;
    }
}
