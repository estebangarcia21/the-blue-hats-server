package com.thebluehats.server.game.enchants

import com.google.inject.Inject
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
import org.bukkit.entity.Player
import java.util.*

class Solitude @Inject constructor(
    private val damageManager: DamageManager, playerDamageTrigger: PlayerDamageTrigger,
    arrowDamageTrigger: ArrowDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf(playerDamageTrigger, arrowDamageTrigger), arrayOf(
        damageManager
    )
) {
    private val damageReduction = EnchantProperty(.4f, .5f, .6f)
    private val playersNeeded = EnchantProperty(1, 2, 2)

    override val name: String get() = "Solitude"
    override val enchantReferenceName: String get() = "Solitude"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = true
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.LEATHER_LEGGINGS)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGEE

    override fun execute(data: DamageEventEnchantData) {
        val event = data.event
        val damagee = data.damagee
        val level = data.level
        val entities = damagee.getNearbyEntities(7.0, 7.0, 7.0)
        val players: MutableList<Player> = ArrayList()
        for (entity in entities) {
            if (entity is Player) {
                if (entity !== damagee) {
                    players.add(entity)
                }
            }
        }
        if (players.size <= playersNeeded.getValueAtLevel(level)) {
            damageManager.reduceDamageByPercentage(event, damageReduction.getValueAtLevel(level).toDouble())
        }
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Recieve <blue>-{0}</blue> damage when only<br/>one other player is within 7<br/>blocks"
        )
        enchantLoreParser.setSingleVariable("40%", "50%", "60%")
        return enchantLoreParser.parseForLevel(level)
    }
}