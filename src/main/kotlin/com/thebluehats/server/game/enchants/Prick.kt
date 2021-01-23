package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.at
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import org.bukkit.Material
import java.util.*
import javax.inject.Inject

class Prick @Inject constructor(
    private val damageManager: DamageManager,
    playerDamageTrigger: PlayerDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf(playerDamageTrigger), arrayOf(damageManager)
) {
    private val damage = EnchantProperty(.5, .75, 1.0)

    override val name: String get() = "Prick"
    override val enchantReferenceName: String get() = "prick"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.A
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.LEATHER_LEGGINGS)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGEE

    override fun execute(data: DamageEventEnchantData) {
        val level = data.level

        damageManager.doTrueDamage(data.damager, damage at level, data.damagee)
    }

    override fun getDescription(level: Int): ArrayList<String> {
        TODO("Not yet implemented")
    }
}