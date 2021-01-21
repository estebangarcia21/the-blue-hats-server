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
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import com.thebluehats.server.game.utils.EntityValidator
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

class FancyRaider @Inject constructor(
    private val damageManager: DamageManager,
    playerDamageTrigger: PlayerDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf<DamageEnchantTrigger>(playerDamageTrigger), arrayOf<EntityValidator>(
        damageManager
    )
) {
    private val percentDamageIncrease = EnchantProperty(0.05f, 0.09f, 0.15f)
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
        if (player.inventory.helmet != null) {
            if (player.inventory.helmet.type == Material.LEATHER_HELMET) {
                return true
            }
        }
        if (player.inventory.leggings != null) {
            if (player.inventory.leggings.type == Material.LEATHER_LEGGINGS) {
                return true
            }
        }
        return false
    }

    override fun getName(): String {
        return "Fancy Raider"
    }

    override fun getEnchantReferenceName(): String {
        return "Fancyraider"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Deal <red>+</red> damage vs. players<br/>wearing leather armor"
        )
        enchantLoreParser.setSingleVariable("5%", "9%", "15%")
        return enchantLoreParser.parseForLevel(level)
    }

    override fun isDisabledOnPassiveWorld(): Boolean {
        return false
    }

    override fun getEnchantGroup(): EnchantGroup {
        // TODO Determine Enchant Group
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