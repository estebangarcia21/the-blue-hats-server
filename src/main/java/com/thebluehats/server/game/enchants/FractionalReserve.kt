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
import org.bukkit.Material
import java.util.*

class FractionalReserve @Inject constructor(
    private val damageManager: DamageManager, playerDamageTrigger: PlayerDamageTrigger,
    arrowDamageTrigger: ArrowDamageTrigger, private val pitData: PerformanceStatsService
) : DamageTriggeredEnchant(
    arrayOf(playerDamageTrigger, arrowDamageTrigger), arrayOf(damageManager)
) {
    private val maximumDamageReduction = EnchantProperty(.15, .21, .30)

    override val name: String get() = "Fractional Reserve"
    override val enchantReferenceName: String get() = "frac"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.A
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.LEATHER_LEGGINGS)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGEE

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

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Receive <blue>-1% damage</blue> per<br/><gold>10,000g</gold> you have (<blue>-{0}</blue><br/>max)"
        )

        enchantLoreParser.setSingleVariable("15%", "21%", "30%")

        return enchantLoreParser.parseForLevel(level)
    }
}