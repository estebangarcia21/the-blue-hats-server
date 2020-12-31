package com.thebluehats.server.game.world;

import com.thebluehats.server.game.managers.world.regionmanager.RegionManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import javax.inject.Inject;

public class WorldProtection implements Listener {
    private final RegionManager regionManager;

    @Inject
    public WorldProtection(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler
    public void onBlockBread(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.OBSIDIAN) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() != Material.OBSIDIAN
                && event.getBlockPlaced().getType() != Material.COBBLESTONE) {
            event.setCancelled(true);
            return;
        }

        if (regionManager.locationisInSpawn(event.getBlockPlaced().getLocation())) {
            event.setCancelled(true);
        }
    }
}
