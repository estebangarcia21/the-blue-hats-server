package com.thebluehats.server.game.enchants

import com.google.inject.Inject
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

class Lifesteal @Inject constructor(
    private val damageManager: DamageManager,
    playerDamageTrigger: PlayerDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf(playerDamageTrigger), arrayOf(
        damageManager
    )
) {
    private val healPercentage = EnchantProperty(0.04f, 0.08f, 0.013f)

    override val name: String get() = "Lifesteal"
    override val enchantReferenceName: String get() = "Lifesteal"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val event = data.event
        val damager = data.damager
        val level = data.level
        damager.health = Math.min(
            Math.min(
                damager.health + damageManager.getDamageFromEvent(event) * healPercentage.getValueAtLevel(
                    level
                ), 3.0
            ),
            damager.maxHealth
        )
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Heal for <red>{0}</red> of damage dealt up<br/>to <red>1.5❤</red>"
        )
        enchantLoreParser.setSingleVariable("4", "8", "13")
        return enchantLoreParser.parseForLevel(level)
    }
}