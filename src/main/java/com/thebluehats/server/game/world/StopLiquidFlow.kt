package com.thebluehats.server.game.world

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFromToEvent

class StopLiquidFlow : Listener {
    @EventHandler
    fun onFlow(event: BlockFromToEvent) {
        if (event.block.type == Material.STATIONARY_WATER || event.block.type == Material.STATIONARY_LAVA) {
            event.isCancelled = true
        }
    }
}