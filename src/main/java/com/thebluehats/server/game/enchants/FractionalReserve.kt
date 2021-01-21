package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.api.daos.PerformanceStatsService
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

class FractionalReserve @Inject constructor(
    private val damageManager: DamageManager, playerDamageTrigger: PlayerDamageTrigger,
    arrowDamageTrigger: ArrowDamageTrigger, private val pitData: PerformanceStatsService
) : DamageTriggeredEnchant(
    arrayOf(playerDamageTrigger, arrowDamageTrigger), arrayOf<EntityValidator>(
        damageManager
    )
) {
    private val maximumDamageReduction = EnchantProperty(.15, .21, .30)
    override fun execute(data: DamageEventEnchantData) {
        val event = data.event
        val damagee = data.damagee
        val level = data.level
        var damageReduction = 0.0
        var i = 10000
        while (i <= pitData.getPlayerGold(damagee)) {
            damageReduction += .10
            i += 10000
        }
        if (damageReduction > maximumDamageReduction.getValueAtLevel(level)) {
            damageReduction = maximumDamageReduction.getValueAtLevel(level)
        }
        damageManager.reduceDamageByPercentage(event, damageReduction)
    }

    override fun getName(): String {
        return "Fractional Reserve"
    }

    override fun getEnchantReferenceName(): String {
        return "Frac"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Recieve <blue>-1% damage</blue> per<br/><gold>10,000g</gold> you have (<blue>-{0}</blue><br/>max)"
        )
        enchantLoreParser.setSingleVariable("15%", "21%", "30%")
        return enchantLoreParser.parseForLevel(level)
    }

    override fun isDisabledOnPassiveWorld(): Boolean {
        return false
    }

    override fun getEnchantGroup(): EnchantGroup {
        // TODO Group needs to be changed to AUCTION
        return EnchantGroup.A
    }

    override fun isRareEnchant(): Boolean {
        return false
    }

    override fun getEnchantItemTypes(): Array<Material> {
        return arrayOf(Material.LEATHER_LEGGINGS)
    }

    override fun getEnchantHolder(): EnchantHolder {
        return EnchantHolder.DAMAGEE
    }
}