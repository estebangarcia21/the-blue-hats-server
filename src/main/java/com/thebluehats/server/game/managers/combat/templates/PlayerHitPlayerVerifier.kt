package com.thebluehats.server.game.managers.combat.templates

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class PlayerHitPlayerVerifier : EventVerifier<EntityDamageByEntityEvent> {
    override fun verify(event: EntityDamageByEntityEvent): Boolean {
        return event.damager is Player && event.entity is Player
    }
}