package com.thebluehats.server.game.enchants.args.custom;

import org.bukkit.entity.Player;

public class HealerArgs {
    private final Player damager;
    private final Player damaged;
    private final int healAmount;

    public HealerArgs(Player damager, Player damaged, int healAmount) {
        this.damager = damager;
        this.damaged = damaged;
        this.healAmount = healAmount;
    }

    public Player getDamager() {
        return damager;
    }

    public Player getDamaged() {
        return damaged;
    }

    public int getHealAmount() {
        return healAmount;
    }
}
