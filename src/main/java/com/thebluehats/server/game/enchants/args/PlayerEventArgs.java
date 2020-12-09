package com.thebluehats.server.game.enchants.args;

import org.bukkit.entity.Player;

public class PlayerEventArgs {
    private final Player player;

    public PlayerEventArgs(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
