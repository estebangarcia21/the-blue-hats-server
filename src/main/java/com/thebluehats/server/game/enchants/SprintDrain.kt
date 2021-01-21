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
    DamageTriggeredEnchant(arrayOf<DamageEnchantTrigger>(arrowDamageTrigger)) {
    private val speedDuration = EnchantProperty(5, 5, 7)
    private val speedAmplifier = EnchantProperty(0, 0, 1)
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

    override fun getName(): String {
        return "Sprint Drain"
    }

    override fun getEnchantReferenceName(): String {
        return "Sprintdrain"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Arrow shots grant you <yellow>Speed {0}</yellow><br/>({1}s)"
        )
        enchantLoreParser.addTextIf(level != 1, " and apply <blue>Slowness I</blue><br/>(3s)")
        val variables: Array<Array<String>> = arrayOfNulls(2)
        variables[0] = arrayOf("I", "I", "II")
        variables[1] = arrayOf("", "5", "7")
        enchantLoreParser.setVariables(variables)
        return enchantLoreParser.parseForLevel(level)
    }

    override fun isDisabledOnPassiveWorld(): Boolean {
        return false
    }

    override fun getEnchantGroup(): EnchantGroup {
        return EnchantGroup.C
    }

    override fun isRareEnchant(): Boolean {
        return false
    }

    override fun getEnchantItemTypes(): Array<Material> {
        return arrayOf(Material.BOW)
    }

    override fun getEnchantHolder(): EnchantHolder {
        return EnchantHolder.DAMAGER
    }
}