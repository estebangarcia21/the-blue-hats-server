package com.thebluehats.server.game.other

import org.bukkit.Material
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent

class Bread : Listener {
    @EventHandler
    fun onEat(event: PlayerItemConsumeEvent) {
        if (event.item.type == Material.BREAD) {
            event.player.health = event.player.maxHealth.coerceAtMost(event.player.health + 8)

            val player = (event.player as CraftPlayer).handle
            player.absorptionHearts = (player.absorptionHearts + 2).coerceAtMost(4f)
        }
    }
}