package com.thebluehats.server.game.world;

import com.thebluehats.server.game.managers.enchants.GlobalTimerListener;
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;

public class PlayableArea implements GlobalTimerListener {
    private final RegionManager regionManager;
    private final JavaPlugin plugin;

    @Inject
    public PlayableArea(RegionManager regionManager, JavaPlugin plugin) {
        this.regionManager = regionManager;
        this.plugin = plugin;
    }

    @Override
    public void onTick(Player player) {
        if (!regionManager.entityIsInPlayableArea(player)) {
            plugin.getServer().getPluginManager().callEvent(new PlayerDeathEvent(player, null, 0, ""));

            player.sendMessage(ChatColor.RED + "Congratulations! You went out of the map!");
        }
    }
}
