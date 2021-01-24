package com.thebluehats.server.game.managers.enchants.processedevents

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

open class CastedEntityDamageByEntityEvent(
    event: EntityDamageByEntityEvent,
    val damager: Player,
    val damagee: Player
) : CastedEvent<EntityDamageByEntityEvent>(event)