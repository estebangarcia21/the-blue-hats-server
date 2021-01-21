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
import com.thebluehats.server.game.managers.enchants.HitCounter
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import com.thebluehats.server.game.utils.EntityValidator
import org.bukkit.Material
import org.bukkit.Sound
import java.util.*

class ComboDamage @Inject constructor(
    private val damageManager: DamageManager, private val hitCounter: HitCounter,
    playerDamageTrigger: PlayerDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf<DamageEnchantTrigger>(playerDamageTrigger),
    arrayOf<EntityValidator>(damageManager)
) {
    private val damageAmount = EnchantProperty(.2f, .3f, .45f)
    private val hitsNeeded = EnchantProperty(4, 3, 3)
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

    override fun getName(): String {
        return "Combo: Damage"
    }

    override fun getEnchantReferenceName(): String {
        return "Combodamage"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Every <yellow>{0}</yellow> strike deals<br/><red>{1}</red> damage"
        )
        val variables: Array<Array<String>> = arrayOfNulls(2)
        variables[0] = arrayOf("fourth", "third", "third")
        variables[1] = arrayOf("+20%", "+30%", "+45%")
        enchantLoreParser.setVariables(variables)
        return enchantLoreParser.parseForLevel(level)
    }

    override fun isDisabledOnPassiveWorld(): Boolean {
        return false
    }

    override fun getEnchantGroup(): EnchantGroup {
        return EnchantGroup.A
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