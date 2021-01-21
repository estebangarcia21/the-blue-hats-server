package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.CalculationMode
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.HitCounter
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import org.bukkit.Sound
import java.util.*

class ComboDamage @Inject constructor(
    private val damageManager: DamageManager, private val hitCounter: HitCounter,
    playerDamageTrigger: PlayerDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf(playerDamageTrigger), arrayOf(damageManager)
) {
    private val damageAmount = EnchantProperty(.2f, .3f, .45f)
    private val hitsNeeded = EnchantProperty(4, 3, 3)

    override val name: String get () = "Combo: Damage"
    override val enchantReferenceName: String get() = "Combodamage"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.A
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val damager = data.damager

        hitCounter.addOne(damager)

        if (hitCounter.hasHits(damager, hitsNeeded.getValueAtLevel(data.level))) {
            damager.playSound(damager.location, Sound.DONKEY_HIT, 1f, 0.5f)

            damageManager.addDamage(
                data.event, damageAmount.getValueAtLevel(data.level).toDouble(),
                CalculationMode.ADDITIVE
            )
        }
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Every <yellow>{0}</yellow> strike deals<br/><red>{1}</red> damage"
        )

        val vars = varMatrix()

        vars add Var(0, "fourth", "third", "third")
        vars add Var(1, "+20%", "+30%", "+45%")

        enchantLoreParser.setVariables(vars)

        return enchantLoreParser.parseForLevel(level)
    }
}