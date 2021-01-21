package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.HitCounter
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class ComboSwift @Inject constructor(private val hitCounter: HitCounter, playerDamageTrigger: PlayerDamageTrigger) :
    DamageTriggeredEnchant(arrayOf<DamageEnchantTrigger>(playerDamageTrigger)) {
    private val speedTime = EnchantProperty(3, 4, 5)
    private val speedAmplifier = EnchantProperty(0, 1, 1)
    private val hitsNeeded = EnchantProperty(4, 3, 3)
    override fun execute(data: DamageEventEnchantData) {
        val damager = data.damager
        val level = data.level
        hitCounter.addOne(damager)
        if (hitCounter.hasHits(damager, hitsNeeded.getValueAtLevel(level))) {
            damager.addPotionEffect(
                PotionEffect(
                    PotionEffectType.SPEED, speedTime.getValueAtLevel(level) * 20,
                    speedAmplifier.getValueAtLevel(level)
                ), true
            )
        }
    }

    override fun getName(): String {
        return "Combo: Swift"
    }

    override fun getEnchantReferenceName(): String {
        return "Comboswift"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Every <yellow>{0}</yellow> strike gain<br/><yellow>Speed {1}</yellow> ({2}s)"
        )
        val variables: Array<Array<String>> = arrayOfNulls(3)
        variables[0] = arrayOf("fourth", "third", "third")
        variables[1] = arrayOf("I", "II", "III")
        variables[2] = arrayOf("3", "4", "5")
        enchantLoreParser.setVariables(variables)
        return enchantLoreParser.parseForLevel(level)
    }

    override fun isDisabledOnPassiveWorld(): Boolean {
        return false
    }

    override fun getEnchantGroup(): EnchantGroup {
        return EnchantGroup.B
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