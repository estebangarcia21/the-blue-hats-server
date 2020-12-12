package com.thebluehats.server.game.enchants.args.common;

import org.bukkit.entity.Player;

public class PlayerArgs {
    private final Player player;

    public PlayerArgs(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
