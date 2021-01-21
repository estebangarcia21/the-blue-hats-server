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

class Shark @Inject constructor(private val damageManager: DamageManager, playerDamageTrigger: PlayerDamageTrigger) :
    DamageTriggeredEnchant(
        arrayOf(playerDamageTrigger), arrayOf(damageManager)
    ) {
    private val percentDamageIncrease = EnchantProperty(0.02f, 0.04f, 0.07f)
    override fun execute(data: DamageEventEnchantData) {
        val event = data.event
        val damager = data.damager
        val level = data.level
        val entities = damager.getNearbyEntities(12.0, 12.0, 12.0)
        val players: MutableList<Player> = ArrayList()
        for (entity in entities) {
            if (entity is Player) {
                if (entity !== damager) {
                    players.add(entity)
                }
            }
        }
        damageManager.addDamage(
            event, (percentDamageIncrease.getValueAtLevel(level) * players.size).toDouble(),
            CalculationMode.ADDITIVE
        )
    }

    override fun getName(): String {
        return "Shark"
    }

    override fun getEnchantReferenceName(): String {
        return "Shark"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Deal <red>+{0}</red> damage per other<br/>player below <red>6❤</red> within 12<br/>blocks"
        )
        enchantLoreParser.setSingleVariable("2%", "4%", "7%")
        return enchantLoreParser.parseForLevel(level)
    }

    override fun isDisabledOnPassiveWorld(): Boolean {
        return false
    }

    override fun getEnchantGroup(): EnchantGroup {
        // TODO Determine EnchantGroup
        return EnchantGroup.B
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