package com.thebluehats.server.game.managers.enchants.processedevents

import com.google.common.collect.ImmutableMap
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class DamageEventEnchantData(
    event: EntityDamageByEntityEvent,
    damager: Player,
    damagee: Player,
    val levelMap: ImmutableMap<Material, Int>
) : CastedEntityDamageByEntityEvent(event, damager, damagee) {
    val level: Int
        get() = levelMap.values.asList()[0]
}