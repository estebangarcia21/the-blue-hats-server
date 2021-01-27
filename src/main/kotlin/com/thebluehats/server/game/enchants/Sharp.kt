package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.CalculationMode
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

class Sharp @Inject constructor(private val damageManager: DamageManager, playerDamageTrigger: PlayerDamageTrigger) :
    DamageTriggeredEnchant(
        arrayOf(playerDamageTrigger), arrayOf(damageManager)
    ) {
    private val percentDamageIncrease = EnchantProperty(0.04, 0.07, 0.12)

    override val name: String get() = "Sharp"
    override val enchantReferenceName: String get() = "sharp"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.A
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val event = data.event
        val level = data.level
        damageManager.addDamage(
            event,
            percentDamageIncrease.getValueAtLevel(level),
            CalculationMode.ADDITIVE
        )
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("Deal <red>+{0}</red> melee damage")
        enchantLoreParser.setSingleVariable("4%", "7%", "12%")
        return enchantLoreParser.parseForLevel(level)
    }
}