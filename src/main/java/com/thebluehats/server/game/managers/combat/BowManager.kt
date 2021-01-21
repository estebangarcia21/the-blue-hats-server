package com.thebluehats.server.game.managers.combat

import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import java.util.*

class BowManager : Listener {
    private val data = HashMap<Arrow, PlayerInventory>()

    @EventHandler
    fun onArrowShoot(event: EntityShootBowEvent) {
        if (event.projectile is Arrow) {
            val arrow = event.projectile as Arrow
            if (arrow.shooter is Player) {
                data[arrow] = (arrow.shooter as Player).inventory
            }
        }
    }

    fun registerArrow(arrow: Arrow, player: Player) {
        data[arrow] = player.inventory
    }

    fun getBowFromArrow(arrow: Arrow): ItemStack {
        for (arr in data.keys) {
            if (arr == arrow) {
                return data[arr]!!.itemInHand
            }
        }
        return ItemStack(Material.BOW)
    }

    fun getLeggingsFromArrow(arrow: Arrow): ItemStack {
        return data[arrow]!!.leggings
    }
}