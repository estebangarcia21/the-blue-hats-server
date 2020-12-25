package com.thebluehats.server.game.utils;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public interface DataInitializer extends Listener {
    void initializeDataOnPlayerJoin(PlayerJoinEvent event);
}
