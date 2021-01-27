package com.thebluehats.server.game.world

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class NoFallDamage : Listener {
    @EventHandler
    fun onFall(event: EntityDamageEvent) {
        event.isCancelled = event.cause == EntityDamageEvent.DamageCause.FALL
    }
}