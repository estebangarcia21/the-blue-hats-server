package com.thebluehats.server.game.other

import com.thebluehats.server.game.managers.enchants.Timer
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager
import com.thebluehats.server.game.utils.PluginLifecycleListener
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import javax.inject.Inject

class Obsidian @Inject constructor(private val regionManager: RegionManager, private val timer: Timer<Block>) :
    Listener, PluginLifecycleListener {
    @EventHandler(priority = EventPriority.MONITOR)
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (regionManager.locationisInSpawn(event.blockPlaced.location)) {
            return
        }
        val block = event.blockPlaced
        if (block.type == Material.OBSIDIAN) {
            timer.start(block, (120 * 20).toLong(), false) { event.blockPlaced.type = Material.AIR }
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        if (event.block.type == Material.OBSIDIAN) {
            for (obsidianBlocks in timer.keys) {
                if (obsidianBlocks == event.block) {
                    timer.cancel(block)
                }
            }
        }
    }

    override fun onPluginStart() {}
    override fun onPluginEnd() {
        for (block in timer.keys) {
            block.type = Material.AIR
        }
    }
}