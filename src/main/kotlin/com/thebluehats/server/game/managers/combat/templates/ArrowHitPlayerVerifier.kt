package com.thebluehats.server.game.managers.combat.templates

import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class ArrowHitPlayerVerifier : EventVerifier<EntityDamageByEntityEvent> {
    override fun verify(event: EntityDamageByEntityEvent): Boolean {
        val damager = event.damager
        val damagee = event.entity
        if (damager is Arrow && damagee is Player) {
            return damager.shooter is Player
        }
        return false
    }
}