package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.CalculationMode
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

class FancyRaider @Inject constructor(
    private val damageManager: DamageManager,
    playerDamageTrigger: PlayerDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf(playerDamageTrigger), arrayOf(damageManager)
) {
    private val percentDamageIncrease = EnchantProperty(0.05f, 0.09f, 0.15f)

    override val name: String get() = "Fancy Raider"
    override val enchantReferenceName: String get() = "fancy-raider"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.A
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val damagee = data.damagee

        if (playerHasLeatherPiece(damagee)) {
            damageManager.addDamage(
                data.event, percentDamageIncrease.getValueAtLevel(data.level).toDouble(),
                CalculationMode.ADDITIVE
            )
        }
    }

    private fun playerHasLeatherPiece(player: Player): Boolean {
        val inv = player.inventory

        return arrayOf(inv.helmet, inv.leggings).any { o ->
            if (o == null) return false

            val tokens = o.type.name.split("_")

            return tokens[0].equals("leather", ignoreCase = true)
        }
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Deal <red>+{0}%</red> damage vs. players<br/>wearing leather armor"
        )

        enchantLoreParser.setSingleVariable("5", "9", "15")

        return enchantLoreParser.parseForLevel(level)
    }
}