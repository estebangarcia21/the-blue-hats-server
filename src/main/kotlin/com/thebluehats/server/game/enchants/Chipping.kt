package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.at
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import java.util.*
import org.bukkit.Material

class Chipping
@Inject
constructor(private val damageManager: DamageManager, arrowDamageTrigger: ArrowDamageTrigger) :
        DamageTriggeredEnchant(arrayOf(arrowDamageTrigger), arrayOf(damageManager)) {
    private val damageAmount = EnchantProperty(1.0, 2.0, 3.0)

    override val name: String get() = "Chipping"
    override val enchantReferenceName: String get() = "chipping"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = true
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.BOW)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val damagee = data.damagee
        val damager = data.damager
        val level = data.level
        damageManager.doTrueDamage(damagee, damageAmount at level, damager)
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("Deals <red>{0}</red> extra true damage")

        enchantLoreParser.setSingleVariable("0.5❤", "1.0❤", "1.5❤")

        return enchantLoreParser.parseForLevel(level)
    }
}
