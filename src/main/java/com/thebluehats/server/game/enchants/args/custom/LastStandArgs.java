package com.thebluehats.server.game.enchants.args.custom;

import org.bukkit.entity.Player;

public class LastStandArgs {
    private final Player damaged;
    private final int effectAmplifier;

    public LastStandArgs(Player damaged, int effectAmplifier) {
        this.damaged = damaged;
        this.effectAmplifier = effectAmplifier;
    }

    public Player getDamaged() {
        return damaged;
    }

    public int getEffectAmplifier() {
        return effectAmplifier;
    }
}
