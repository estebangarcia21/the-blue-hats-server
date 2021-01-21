package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger
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

class SprintDrain @Inject constructor(arrowDamageTrigger: ArrowDamageTrigger) :
    DamageTriggeredEnchant(arrayOf(arrowDamageTrigger)) {
    private val speedDuration = EnchantProperty(5, 5, 7)
    private val speedAmplifier = EnchantProperty(0, 0, 1)

    override val name: String get() = "Sprint Drain"
    override val enchantReferenceName: String get() = "Sprintdrain"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.A
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.BOW)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val level = data.level
        data.damager.addPotionEffect(
            PotionEffect(
                PotionEffectType.SPEED,
                speedDuration.getValueAtLevel(level) * 20, speedAmplifier.getValueAtLevel(level)
            ), true
        )
        if (level == 1) return
        data.damagee.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 60, 0))
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Arrow shots grant you <yellow>Speed {0}</yellow><br/>({1}s)"
        )
        enchantLoreParser.addTextIf(level != 1, " and apply <blue>Slowness I</blue><br/>(3s)")
        val variables = varMatrix()
        variables add Var(0, "I", "I", "II")
        variables add Var(1, "", "5", "7")
        enchantLoreParser.setVariables(variables)
        return enchantLoreParser.parseForLevel(level)
    }
}