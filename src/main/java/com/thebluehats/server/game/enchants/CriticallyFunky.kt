package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.CalculationMode
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import com.thebluehats.server.game.utils.EntityValidator
import org.bukkit.Material
import java.util.*

class CriticallyFunky @Inject constructor(
    private val damageManager: DamageManager, playerDamageTrigger: PlayerDamageTrigger,
    arrowDamageTrigger: ArrowDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf(playerDamageTrigger, arrowDamageTrigger), arrayOf<EntityValidator>(
        damageManager
    )
) {
    private val damageReduction = EnchantProperty(0.35f, 0.35f, 0.6f)
    private val damageIncrease = EnchantProperty(0f, .14f, .3f)
    private val extraDamageQueue = ArrayList<UUID>()
    override fun execute(data: DamageEventEnchantData) {
        val event = data.event
        val damager = data.damager
        val level = data.level
        if (!damageManager.isCriticalHit(damager)) return
        if (extraDamageQueue.contains(damager.uniqueId)) {
            damageManager.addDamage(event, damageIncrease.getValueAtLevel(level).toDouble(), CalculationMode.ADDITIVE)
            extraDamageQueue.remove(damager.uniqueId)
        }
        if (level != 1) {
            extraDamageQueue.add(event.entity.uniqueId)
        }
        damageManager.reduceDamageByPercentage(event, damageReduction.getValueAtLevel(level).toDouble())
        damageManager.removeExtraCriticalDamage(event)
    }

    override fun getName(): String {
        return "Critically Funky"
    }

    override fun getEnchantReferenceName(): String {
        return "Critfunky"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Critical hits against you deal<br/><blue>{0}</blue> of the damage they<br/> normally would"
        )
        enchantLoreParser.addTextIf(level != 1, " and empower your<br/>next strike for <red>{1}</red> damage")
        val variables: Array<Array<String>> = arrayOfNulls(2)
        variables[0] = arrayOf("65%", "65%", "40%")
        variables[1] = arrayOf("", "14%", "30%")
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
        return arrayOf(Material.LEATHER_LEGGINGS)
    }

    override fun getEnchantHolder(): EnchantHolder {
        return EnchantHolder.DAMAGER
    }
}