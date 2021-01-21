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

class DiamondStomp @Inject constructor(
    private val damageManager: DamageManager,
    playerDamageTrigger: PlayerDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf(playerDamageTrigger), arrayOf(damageManager)
) {
    private val percentDamageIncrease = EnchantProperty(0.7, 0.12, 0.25)

    override val name: String get() = "Diamond Stomp"
    override val enchantReferenceName: String get() = "Diamondstomp"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.A
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val damagee = data.damagee

        if (playerHasDiamondPiece(damagee)) {
            damageManager.addDamage(
                data.event, percentDamageIncrease.getValueAtLevel(data.level),
                CalculationMode.ADDITIVE
            )
        }
    }

    private fun playerHasDiamondPiece(player: Player): Boolean {
        val inventory = player.inventory

        return arrayOf(inventory.helmet, inventory.chestplate, inventory.leggings, inventory.boots).any { o ->
            if (o == null) return false

            val tokens = o.type.name.split("_")

            return tokens[0].equals("diamond", ignoreCase = true)
        }
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Deal <red>+{0}</red> damage vs. players<br/>wearing diamond armor"
        )

        enchantLoreParser.setSingleVariable("7%", "12%", "25%")

        return enchantLoreParser.parseForLevel(level)
    }
}