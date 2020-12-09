package com.thebluehats.server.game.enchants.args;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerAndDamageEventArgs {
    private Player player;
    private EntityDamageByEntityEvent event;

    public PlayerAndDamageEventArgs(Player player, EntityDamageByEntityEvent event) {
        this.player = player;
        this.event = event;
    }

    public Player getPlayer() {
        return player;
    }

    public EntityDamageByEntityEvent getEvent() {
        return event;
    }
}
