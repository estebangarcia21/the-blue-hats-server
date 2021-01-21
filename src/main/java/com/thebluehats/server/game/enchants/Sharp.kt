package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.CalculationMode
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import com.thebluehats.server.game.utils.EntityValidator
import org.bukkit.Material
import java.util.*

class Sharp @Inject constructor(private val damageManager: DamageManager, playerDamageTrigger: PlayerDamageTrigger) :
    DamageTriggeredEnchant(
        arrayOf<DamageEnchantTrigger>(playerDamageTrigger), arrayOf<EntityValidator>(
            damageManager
        )
    ) {
    private val percentDamageIncrease = EnchantProperty(0.04f, 0.07f, 0.12f)
    override fun execute(data: DamageEventEnchantData) {
        val event = data.event
        val level = data.level
        damageManager.addDamage(
            event,
            percentDamageIncrease.getValueAtLevel(level).toDouble(),
            CalculationMode.ADDITIVE
        )
    }

    override fun getName(): String {
        return "Sharp"
    }

    override fun getEnchantReferenceName(): String {
        return "Sharp"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("Deal <red>+{0}</red> melee damage")
        enchantLoreParser.setSingleVariable("4%", "7%", "12%")
        return enchantLoreParser.parseForLevel(level)
    }

    override fun isDisabledOnPassiveWorld(): Boolean {
        return false
    }

    override fun getEnchantGroup(): EnchantGroup {
        // TODO Determine EnchantGroup
        return EnchantGroup.A
    }

    override fun isRareEnchant(): Boolean {
        return false
    }

    override fun getEnchantItemTypes(): Array<Material> {
        return arrayOf(Material.GOLD_SWORD)
    }

    override fun getEnchantHolder(): EnchantHolder {
        return EnchantHolder.DAMAGER
    }
}