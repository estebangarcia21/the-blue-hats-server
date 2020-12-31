package com.thebluehats.server.game.other;

import com.thebluehats.server.game.managers.enchants.Timer;
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import javax.inject.Inject;

public class Obsidian {
    private final RegionManager regionManager;
    private final Timer<Block> timer;

    @Inject
    public Obsidian(RegionManager regionManager, Timer<Block> timer) {
        this.regionManager = regionManager;
        this.timer = timer;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (regionManager.locationisInSpawn(event.getBlockPlaced().getLocation())) {
            return;
        }

        if (event.getBlockPlaced().getType() == Material.OBSIDIAN) {
            timer.start(null, 120 * 20, () -> event.getBlockPlaced().setType(Material.AIR));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (event.getBlock().getType() == Material.OBSIDIAN) {
            for (Block obsidianBlocks : timer.getKeys()) {
                if (obsidianBlocks.equals(event.getBlock())) {
                    timer.cancel(block);
                }
            }
        }
    }

    public void removeObsidian() {
        for (Block block : timer.getKeys()) {
            block.setType(Material.AIR);
        }
    }
}
