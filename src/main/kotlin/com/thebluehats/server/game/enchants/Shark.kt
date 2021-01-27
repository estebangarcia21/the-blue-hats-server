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

class Shark @Inject constructor(private val damageManager: DamageManager, playerDamageTrigger: PlayerDamageTrigger) :
    DamageTriggeredEnchant(
        arrayOf(playerDamageTrigger), arrayOf(damageManager)
    ) {
    private val percentDamageIncrease = EnchantProperty(0.02f, 0.04f, 0.07f)

    override val name: String get() = "Shark"
    override val enchantReferenceName: String get() = "shark"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

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

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Deal <red>+{0}</red> damage per other<br/>player below <red>6❤</red> within 12<br/>blocks"
        )
        enchantLoreParser.setSingleVariable("2%", "4%", "7%")
        return enchantLoreParser.parseForLevel(level)
    }
}