package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.enchants.CustomEnchant
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.util.Vector
import java.util.*
import javax.inject.Inject

class Instaboom @Inject constructor(private val damageManager: DamageManager, private val customEnchantUtils:
CustomEnchantUtils) : CustomEnchant, Listener {
    override val name: String get() = "Instaboom"
    override val enchantReferenceName: String get() = "instaboom"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = true
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.BOW)

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val data = customEnchantUtils.getItemEnchantData(this, event.player.inventory.leggings)

        if (data.itemHasEnchant()) {
            val level = data.enchantLevel;

            execute(level, event.block.location)
        }
    }

    fun execute(level: Int, location: Location) {
        val radius = 4.0

        location.world.getNearbyEntities(location, radius, radius, radius).forEach(this::applyForce)
    }

    private fun applyForce(e: Entity) {
        if (e !is Player) return

        val player: Player = e

        val velocities = arrayOf(player.velocity.x, player.velocity.y, player.velocity.z)

        player.velocity = Vector(player.velocity.x * velocities[0], player.velocity.y * velocities[1], player
            .velocity.z * velocities[1]
        )
    }

    override fun getDescription(level: Int): ArrayList<String> {
        TODO("Not yet implemented")
    }
}