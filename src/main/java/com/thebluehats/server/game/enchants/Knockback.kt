package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import org.bukkit.util.Vector
import java.util.*

class Knockback @Inject constructor(playerDamageTrigger: PlayerDamageTrigger) :
    DamageTriggeredEnchant(arrayOf(playerDamageTrigger)) {
    private val blocksAmount = EnchantProperty(3, 4, 6)

    override val name: String get() = "Knockback"
    override val enchantReferenceName: String get() = "Knockback"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = true
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val damagee = data.damagee
        val level = data.level
        damagee.velocity = Vector(
            damagee.velocity.x * blocksAmount.getValueAtLevel(level),
            damagee.velocity.y * blocksAmount.getValueAtLevel(level),
            damagee.velocity.z * blocksAmount.getValueAtLevel(level)
        )
    }
    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Increases knockback taken by<br/>enemies by <white>{0} blocks</white>"
        )
        enchantLoreParser.setSingleVariable("3", "6", "9")
        return enchantLoreParser.parseForLevel(level)
    }
}