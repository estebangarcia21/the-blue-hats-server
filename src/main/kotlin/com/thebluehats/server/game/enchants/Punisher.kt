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

class Punisher @Inject constructor(private val damageManager: DamageManager, playerDamageTrigger: PlayerDamageTrigger) :
    DamageTriggeredEnchant(
        arrayOf(playerDamageTrigger), arrayOf(
            damageManager
        )
    ) {
    private val percentDamageIncrease = EnchantProperty(.6f, .12f, .18f)

    override val name: String get() = "Punisher"
    override val enchantReferenceName: String get() = "punisher"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val event = data.event
        val damagee = data.damagee
        val level = data.level
        if (damagee.health < damagee.maxHealth / 2) {
            damageManager.addDamage(
                event,
                percentDamageIncrease.getValueAtLevel(level).toDouble(),
                CalculationMode.ADDITIVE
            )
        }
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Do <red>+{0}</red> damage vs. players<br/>below 50% HP"
        )
        enchantLoreParser.setSingleVariable("7%", "12%", "18%")
        return enchantLoreParser.parseForLevel(level)
    }
}