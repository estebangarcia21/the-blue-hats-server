package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.CalculationMode
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import java.util.*

class Fletching @Inject constructor(private val damageManager: DamageManager, arrowDamageTrigger: ArrowDamageTrigger) :
    DamageTriggeredEnchant(
        arrayOf(arrowDamageTrigger), arrayOf(damageManager)
    ) {
    private val percentDamageIncrease = EnchantProperty(.07f, 0.12f, 0.20f)

    override val name: String get() = "Fletching"
    override val enchantReferenceName: String get() = "fletching"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.A
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.BOW)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        damageManager.addDamage(
            data.event,
            percentDamageIncrease.getValueAtLevel(data.level).toDouble(),
            CalculationMode.ADDITIVE
        )
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("Deal <red>+{0}</red> bow damage")

        enchantLoreParser.setSingleVariable("7%", "12%", "20%")
        
        return enchantLoreParser.parseForLevel(level)
    }
}