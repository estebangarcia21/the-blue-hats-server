package com.thebluehats.server.game.world

import org.bukkit.Bukkit
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import javax.inject.Inject

class ClearArrows @Inject constructor(private val plugin: JavaPlugin) : Listener {
    private val playerToArrows = HashMap<UUID, ArrayList<Arrow>>()
    @EventHandler
    fun onProjectileShoot(event: EntityShootBowEvent) {
        val projectile = event.projectile
        if (event.projectile is Arrow) {
            if ((event.projectile as Arrow).shooter is Player) {
                val player = (event.projectile as Arrow).shooter as Player
                if (!playerToArrows.containsKey(player.uniqueId)) {
                    playerToArrows[player.uniqueId] = ArrayList()
                }
                val data = playerToArrows[player.uniqueId]!!

                // TODO Server config
                val arrowMaximum = 7
                if (data.size >= arrowMaximum) {
                    data[0].remove()
                    data.removeAt(0)
                }
                data.add(event.projectile as Arrow)
                playerToArrows[player.uniqueId] = data
            }
        }
        Bukkit.getServer().scheduler.scheduleSyncDelayedTask(plugin, { projectile?.remove() }, 100L)
    }
}