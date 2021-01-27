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
import com.thebluehats.server.game.utils.Var
import com.thebluehats.server.game.utils.add
import com.thebluehats.server.game.utils.varMatrix
import org.bukkit.Material
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class Peroxide @Inject constructor(playerDamageTrigger: PlayerDamageTrigger, arrowDamageTrigger: ArrowDamageTrigger) :
    DamageTriggeredEnchant(arrayOf(playerDamageTrigger, arrowDamageTrigger)) {
    private val regenDuration = EnchantProperty(5, 8, 8)
    private val regenAmplifier = EnchantProperty(0, 0, 1)

    override val name: String get() = "Peroxide"
    override val enchantReferenceName: String get() = "peroxide"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.LEATHER_LEGGINGS)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGEE

    override fun execute(data: DamageEventEnchantData) {
        val level = data.level
        data.damagee.addPotionEffect(
            PotionEffect(
                PotionEffectType.REGENERATION,
                regenDuration.getValueAtLevel(level) * 20, regenAmplifier.getValueAtLevel(level)
            ), true
        )
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("Gain <red>Regen {0}</red> ({1}s) when hit")

        val variables = varMatrix()
        variables add Var(0, "I", "I", "II")
        variables add Var(1, "5", "8", "8")

        enchantLoreParser.setVariables(variables)

        return enchantLoreParser.parseForLevel(level)
    }
}