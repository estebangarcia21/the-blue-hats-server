package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*
import javax.inject.Inject

class Wasp @Inject constructor(arrowDamageTrigger: ArrowDamageTrigger) :
    DamageTriggeredEnchant(arrayOf(arrowDamageTrigger)) {
    private val weaknessDuration = EnchantProperty(6, 11, 16)
    private val weaknessAmplifier = EnchantProperty(1, 2, 3)

    override val name: String get() = "Wasp"
    override val enchantReferenceName: String get() = "Wasp"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.A
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.BOW)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val level = data.level

        data.damagee.addPotionEffect(
            PotionEffect(
                PotionEffectType.WEAKNESS,
                weaknessDuration.getValueAtLevel(level) * 20, weaknessAmplifier.getValueAtLevel(level)
            ), true
        )
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("Apply <red>Weakness {0}</red> ({1}s) on hit")

        val vars = varMatrix()
        vars add Var(0, "II", "III", "IV")
        vars add Var(1, "6", "11", "16")

        enchantLoreParser.setVariables(vars)

        return enchantLoreParser.parseForLevel(level)
    }
}