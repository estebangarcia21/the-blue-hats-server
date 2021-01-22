package com.thebluehats.server.game.managers.enchants.processedevents

import com.google.common.collect.ImmutableMap
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class DamageEventEnchantData(
    event: EntityDamageByEntityEvent,
    damager: Player,
    damagee: Player,
    private val levelMap: HashMap<Material, Int>
) : CastedEntityDamageByEntityEvent(event, damager, damagee) {
    val level: Int
        get() = levelMap.values.indexOf(0)
}
