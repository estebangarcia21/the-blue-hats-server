package com.thebluehats.server.game.utils

import org.bukkit.entity.Player

class PlayerHealth  {
    companion object Utils {
        infix fun Player.addHealth(value: Double) {
            health = (health + value).coerceAtMost(maxHealth)
        }

        infix fun Player.removeHealth(value: Double) {
            health = (health - value).coerceAtLeast(0.0)
        }

        infix fun Player.setHealth(value: Double) {
            health = value.coerceIn(0.0, maxHealth)
        }
    }
}
