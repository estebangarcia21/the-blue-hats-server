package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import com.thebluehats.server.game.utils.PlayerHealth.Utils.addHealth
import org.bukkit.Material
import java.util.*

class Healer @Inject constructor(playerDamageTrigger: PlayerDamageTrigger) :
    DamageTriggeredEnchant(arrayOf(playerDamageTrigger)) {
    private val healAmount = EnchantProperty(2.0, 4.0, 6.0)

    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER
    override val name: String get() = "Healer"
    override val enchantReferenceName: String get() = "Healer"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = true
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)

    override fun execute(data: DamageEventEnchantData) {
        val level = data.level

        data.damager addHealth healAmount.getValueAtLevel(level)
        data.damagee addHealth healAmount.getValueAtLevel(level)
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Hitting a player <green>heals</green> both you and them for <red>{0}</red>"
        )

        enchantLoreParser.setSingleVariable("1❤", "2❤", "3❤")

        return enchantLoreParser.parseForLevel(level)
    }
}