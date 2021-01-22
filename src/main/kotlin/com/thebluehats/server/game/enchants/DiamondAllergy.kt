package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import java.util.*

class DiamondAllergy @Inject constructor(
    private val damageManager: DamageManager,
    playerDamageTrigger: PlayerDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf(playerDamageTrigger), arrayOf(damageManager)
) {
    private val damageReduction = EnchantProperty(0.10f, 0.20f, 0.30f)

    override val name: String get() = "Diamond Allergy"
    override val enchantReferenceName: String get() = "diamond-allergy"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.A
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.LEATHER_LEGGINGS)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val event = data.event
        val damager = data.damager
        val level = data.level

        if (damager.inventory.itemInHand.type == Material.DIAMOND_SWORD) {
            damageManager.reduceDamageByPercentage(event, damageReduction.getValueAtLevel(level).toDouble())
        }
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Recieve <blue>-{0}</blue> damage from<br/>diamond weapons"
        )

        enchantLoreParser.setSingleVariable("10%", "20%", "30%")

        return enchantLoreParser.parseForLevel(level)
    }
}