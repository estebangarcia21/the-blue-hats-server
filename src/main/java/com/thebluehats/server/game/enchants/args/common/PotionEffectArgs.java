package com.thebluehats.server.game.enchants.args;

import org.bukkit.entity.Player;

public class PotionEffectArgs {
    private Player player;
    private int duration;
    private int amplifier;

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
