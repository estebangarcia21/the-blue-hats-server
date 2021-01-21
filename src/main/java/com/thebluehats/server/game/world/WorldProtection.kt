package com.thebluehats.server.game.world

import com.thebluehats.server.game.managers.world.regionmanager.RegionManager
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import javax.inject.Inject

class WorldProtection @Inject constructor(private val regionManager: RegionManager) : Listener {
    @EventHandler
    fun onBlockBread(event: BlockBreakEvent) {
        if (event.block.type != Material.OBSIDIAN) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (event.blockPlaced.type != Material.OBSIDIAN
            && event.blockPlaced.type != Material.COBBLESTONE
        ) {
            event.isCancelled = true
            return
        }
        if (regionManager.locationisInSpawn(event.blockPlaced.location)) {
            event.isCancelled = true
        }
    }
}