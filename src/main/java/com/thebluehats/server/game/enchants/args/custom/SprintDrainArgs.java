package com.thebluehats.server.game.enchants.args.custom;

import com.thebluehats.server.game.enchants.args.common.PotionEffectArgs;

import org.bukkit.entity.Player;

public class SprintDrainArgs extends PotionEffectArgs {
    private final Player damaged;
    private final int level;

    public SprintDrainArgs(Player damager, Player damaged, int duration, int amplifier, int level) {
        super(damager, duration, amplifier);

        this.damaged = damaged;
        this.level = level;
    }

    public Player getDamaged() {
        return damaged;
    }

    public int getLevel() {
        return level;
    }
}
