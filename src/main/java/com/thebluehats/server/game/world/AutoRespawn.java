package com.thebluehats.server.game.world;

import com.thebluehats.server.game.managers.world.regionmanager.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import javax.inject.Inject;

public class AutoRespawn implements Listener {
    private final JavaPlugin plugin;
    private final RegionManager regionManager;

    @Inject
    public AutoRespawn(JavaPlugin plugin, RegionManager regionManager) {
        this.plugin = plugin;
        this.regionManager = regionManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage("");

        triggerRespawnSequence(event.getEntity());
    }

    public void triggerRespawnSequence(Player player) {
        player.setHealth(player.getMaxHealth());
        player.teleport(regionManager.getSpawnLocation(player));

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }

            player.setFoodLevel(19);

            player.setHealth(player.getMaxHealth());
            ((CraftPlayer) player).getHandle().setAbsorptionHearts(0);

            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));

            player.setVelocity(new Vector(0, 0, 0));
            player.setFireTicks(0);
        }, 1);
    }
}
