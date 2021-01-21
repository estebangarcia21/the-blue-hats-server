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

class DiamondStomp @Inject constructor(
    private val damageManager: DamageManager,
    playerDamageTrigger: PlayerDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf<DamageEnchantTrigger>(playerDamageTrigger), arrayOf<EntityValidator>(
        damageManager
    )
) {
    private val percentDamageIncrease = EnchantProperty(0.7, 0.12, 0.25)
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
        if (player.inventory.helmet != null) {
            if (player.inventory.helmet.type == Material.DIAMOND_HELMET) {
                return true
            }
        }
        if (player.inventory.chestplate != null) {
            if (player.inventory.helmet.type == Material.DIAMOND_CHESTPLATE) {
                return true
            }
        }
        if (player.inventory.leggings != null) {
            if (player.inventory.leggings.type == Material.DIAMOND_LEGGINGS) {
                return true
            }
        }
        return if (player.inventory.boots != null) {
            player.inventory.leggings.type == Material.DIAMOND_LEGGINGS
        } else false
    }

    override fun getName(): String {
        return "Diamond Stomp"
    }

    override fun getEnchantReferenceName(): String {
        return "Diamondstomp"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Deal <red>+{0}</red> damage vs. players<br/>wearing diamond armor"
        )
        enchantLoreParser.setSingleVariable("7%", "12%", "25%")
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