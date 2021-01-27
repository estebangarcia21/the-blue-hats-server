package com.thebluehats.server.game.world

import com.thebluehats.server.game.managers.world.regionmanager.RegionManager
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import javax.inject.Inject

class AutoRespawn @Inject constructor(private val plugin: JavaPlugin, private val regionManager: RegionManager) :
    Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.player.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 0))
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        event.deathMessage = ""
        triggerRespawnSequence(event.entity)
    }

    fun triggerRespawnSequence(player: Player) {
        player.health = player.maxHealth
        player.teleport(regionManager.getSpawnLocation(player))
        Bukkit.getServer().scheduler.scheduleSyncDelayedTask(plugin, {
            for (effect in player.activePotionEffects) {
                player.removePotionEffect(effect.type)
            }
            player.foodLevel = 19
            player.health = player.maxHealth
            (player as CraftPlayer).handle.absorptionHearts = 0f
            player.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 0))
            player.setVelocity(Vector(0, 0, 0))
            player.setFireTicks(0)
        }, 1)
    }
}