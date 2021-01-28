package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.*
import com.thebluehats.server.game.managers.enchants.Timer
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.*
import javax.inject.Inject

class Assassin @Inject constructor(
    private val timer: Timer<UUID>, playerDamageTrigger: PlayerDamageTrigger,
    arrowDamageTrigger: ArrowDamageTrigger
) : DamageTriggeredEnchant(arrayOf(playerDamageTrigger, arrowDamageTrigger)) {
    private val cooldownTime = EnchantProperty(5L, 4L, 3L)

    override val name: String get() = "Assassin"
    override val enchantReferenceName: String get() = "assassin"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.A
    override val isRareEnchant: Boolean get() = true
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.LEATHER_LEGGINGS)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val damager = data.damager
        val playerUuid = damager.uniqueId
        val damagee = data.damagee
        val level = data.level

        if(!damagee.isSneaking) return

        if (!timer.isRunning(playerUuid)) {
            val tpLoc = damager.location.subtract(damager.eyeLocation.direction.normalize())
            tpLoc.y = damager.location.y

            if (tpLoc.block.type == Material.AIR) {
                damagee.teleport(tpLoc)
            } else {
                damagee.teleport(damager)
            }

            damagee.world.playSound(damagee.location, Sound.ENDERMAN_TELEPORT, 1f, 2f)
        }

        timer.start(damagee.uniqueId, cooldownTime at level, seconds = true)
    }

    override fun getDescription(level: Int): ArrayList<String> {
        // TODO Level one is only on arrow
        val enchantLoreParser = EnchantLoreParser(
            "Sneaking teleports you behind<br/>your attacker ({0}s cooldown)"
        )

        enchantLoreParser.setSingleVariable("5", "4", "3")

        return enchantLoreParser.parseForLevel(level)
    }
}
