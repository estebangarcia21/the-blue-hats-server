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

class PainFocus @Inject constructor(
    private val damageManager: DamageManager,
    playerDamageTrigger: PlayerDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf(playerDamageTrigger), arrayOf(
        damageManager
    )
) {
    private val damageIncreasePerHeartLost = EnchantProperty(0.01f, 0.02f, 0.05f)

    override val name: String get() = "Pain Focus"
    override val enchantReferenceName: String get() = "pain-focus"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val event = data.event
        val damager = data.damager
        val level = data.level
        val heartsLost = ((damager.maxHealth - damager.health) / 2).toInt()
        damageManager.addDamage(
            event, (damageIncreasePerHeartLost.getValueAtLevel(level) * heartsLost).toDouble(),
            CalculationMode.ADDITIVE
        )
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Deal <red>+{0}</red> damage per <red>❤</red><br/>you're missing"
        )
        enchantLoreParser.setSingleVariable("1%", "+2%", "5%")
        return enchantLoreParser.parseForLevel(level)
    }
}