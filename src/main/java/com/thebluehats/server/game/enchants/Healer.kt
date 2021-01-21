package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import java.util.*

class Healer @Inject constructor(playerDamageTrigger: PlayerDamageTrigger) :
    DamageTriggeredEnchant(arrayOf(playerDamageTrigger)) {
    private val healAmount = EnchantProperty(2, 4, 6)
    override fun execute(data: DamageEventEnchantData) {
        val damager = data.damager
        val damaged = data.damagee
        val level = data.level
        damager.health = Math.min(damager.health + healAmount.getValueAtLevel(level), damager.maxHealth)
        damaged.health = Math.min(damaged.health + healAmount.getValueAtLevel(level), damaged.maxHealth)
    }

    override fun getName(): String {
        return "Healer"
    }

    override fun getEnchantReferenceName(): String {
        return "Healer"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Hitting a player <green>heals</green> both you and them for <red>{0}</red>"
        )
        enchantLoreParser.setSingleVariable("1❤", "2❤", "3❤")
        return enchantLoreParser.parseForLevel(level)
    }

    override fun isDisabledOnPassiveWorld(): Boolean {
        return true
    }

    override fun getEnchantGroup(): EnchantGroup {
        return EnchantGroup.B
    }

    override fun isRareEnchant(): Boolean {
        return true
    }

    override fun getEnchantItemTypes(): Array<Material> {
        return arrayOf(Material.GOLD_SWORD)
    }

    override fun getEnchantHolder(): EnchantHolder {
        return EnchantHolder.DAMAGER
    }
}