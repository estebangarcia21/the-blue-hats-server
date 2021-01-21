package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class Peroxide @Inject constructor(playerDamageTrigger: PlayerDamageTrigger, arrowDamageTrigger: ArrowDamageTrigger) :
    DamageTriggeredEnchant(arrayOf(playerDamageTrigger, arrowDamageTrigger)) {
    private val regenDuration = EnchantProperty(5, 8, 8)
    private val regenAmplifier = EnchantProperty(0, 0, 1)

    override val name = "Peroxide"
    override val enchantReferenceName = "Peroxide"
    override val isDisabledOnPassiveWorld = true
    override val isRareEnchant = false
    override val enchantGroup = EnchantGroup.B
    override val enchantItemTypes = arrayOf(Material.LEATHER_LEGGINGS)
    override val enchantHolder = EnchantHolder.DAMAGEE

    override fun execute(data: DamageEventEnchantData) {
        val level = data.level
        data.damagee.player.addPotionEffect(
            PotionEffect(
                PotionEffectType.REGENERATION,
                regenDuration.getValueAtLevel(level) * 20, regenAmplifier.getValueAtLevel(level), true
            )
        )
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("Gain <red>Regen {0}</red> ({1}s) when hit")

        val vars = varMatrix()
        vars add Var(0, "I", "I", "II")
        vars add Var(1, "5", "8", "8")

        enchantLoreParser.setVariables(vars)

        return enchantLoreParser.parseForLevel(level)
    }
}