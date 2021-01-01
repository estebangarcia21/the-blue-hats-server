package com.thebluehats.server.game.world;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class StopLiquidFlow implements Listener {
    @EventHandler
    public void onFlow(BlockFromToEvent event) {
        if (event.getBlock().getType() == Material.STATIONARY_WATER || event.getBlock().getType() == Material.STATIONARY_LAVA) {
            event.setCancelled(true);
        }
    }
}