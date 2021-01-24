package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.api.daos.PerformanceStatsService
import com.thebluehats.server.game.managers.combat.CalculationMode
import com.thebluehats.server.game.managers.combat.DamageManager
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
import org.bukkit.Sound
import java.util.*

class Billionaire @Inject constructor(
    private val pitData: PerformanceStatsService,
    private val damageManager: DamageManager,
    playerDamageTrigger: PlayerDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf(playerDamageTrigger), arrayOf(damageManager)
) {
    private val damageIncrease = EnchantProperty(1.33, 1.67, 2.0)
    private val goldNeeded = EnchantProperty(100, 200, 350)

    override val name: String get() = "Billionaire"
    override val enchantReferenceName: String get() = "billionaire"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = true
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val damager = data.damager
        val level = data.level
        val gold = pitData.getPlayerGold(damager)

        if (gold < goldNeeded.getValueAtLevel(level)) return

        pitData.setPlayerGold(damager, gold - goldNeeded.getValueAtLevel(level))
        damageManager.addDamage(data.event, damageIncrease.getValueAtLevel(level), CalculationMode.MULTIPLICATIVE)

        damager.playSound(damager.location, Sound.ORB_PICKUP, 1f, 0.73f)
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Hits with this swords deals <red>{0}x</red><br/>damage but cost <gold>{1}</gold>"
        )

        val vars = varMatrix()
        vars add Var(0, "1.33", "1.67", "2")
        vars add Var(1, "100g", "200g", "350g")

        enchantLoreParser.setVariables(vars)

        return enchantLoreParser.parseForLevel(level)
    }
}