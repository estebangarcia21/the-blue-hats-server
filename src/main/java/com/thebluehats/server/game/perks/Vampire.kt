package com.thebluehats.server.game.perks

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.DamageManager
import org.bukkit.entity.Arrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

class Vampire @Inject constructor(private val damageManager: DamageManager) : Perk() {
    @EventHandler
    fun onHit(event: EntityDamageByEntityEvent) {
        var healthValue = 1
        val player: Player
        when (event.damager.type) {
            EntityType.PLAYER -> {
                if (event.damager !is Player) return
                player = event.damager as Player
                healthValue = 1
            }
            EntityType.ARROW -> {
                val arrow = event.damager as Arrow
                if (arrow.shooter !is Player) return
                player = arrow.shooter as Player
                healthValue = if (arrow.isCritical) 3 else 1
            }
            else -> return
        }
        if (!damageManager.uuidIsInCanceledEvent(player.uniqueId)) return
        player.health = Math.min(player.health + healthValue, player.maxHealth)
    }

    override val name: String
        get() = "Vampire"
    override val cost: Int
        get() = 4000
}