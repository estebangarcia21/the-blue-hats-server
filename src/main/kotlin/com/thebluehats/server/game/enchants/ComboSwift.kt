package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.HitCounter
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import com.thebluehats.server.game.utils.Var
import com.thebluehats.server.game.utils.add
import com.thebluehats.server.game.utils.varMatrix
import org.bukkit.Material
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class ComboSwift @Inject constructor(private val hitCounter: HitCounter, playerDamageTrigger: PlayerDamageTrigger) :
    DamageTriggeredEnchant(arrayOf(playerDamageTrigger)) {
    private val speedTime = EnchantProperty(3, 4, 5)
    private val speedAmplifier = EnchantProperty(0, 1, 1)
    private val hitsNeeded = EnchantProperty(4, 3, 3)

    override val name: String get() = "Combo: Swift"
    override val enchantReferenceName: String get() = "combo-stun"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

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

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Every <yellow>{0}</yellow> strike gain<br/><yellow>Speed {1}</yellow> ({2}s)"
        )

        val vars = varMatrix()
        vars add Var(0, "fourth", "third", "third")
        vars add Var(1, "I", "II", "III")
        vars add Var(2, "3", "4", "5")

        enchantLoreParser.setVariables(vars)

        return enchantLoreParser.parseForLevel(level)
    }
}