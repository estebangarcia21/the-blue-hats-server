package com.thebluehats.server.game.world

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerJoinEvent

class AntiHunger : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        player.maxHealth = 24.0
        player.foodLevel = 19
    }

    @EventHandler
    fun onHunger(event: FoodLevelChangeEvent) {
        event.isCancelled = true
    }
}