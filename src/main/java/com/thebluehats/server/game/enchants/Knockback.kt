package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import org.bukkit.util.Vector
import java.util.*

class Knockback @Inject constructor(playerDamageTrigger: PlayerDamageTrigger) :
    DamageTriggeredEnchant(arrayOf<DamageEnchantTrigger>(playerDamageTrigger)) {
    private val blocksAmount = EnchantProperty(3, 4, 6)
    override fun execute(data: DamageEventEnchantData) {
        val damagee = data.damagee
        val level = data.level
        damagee.velocity = Vector(
            damagee.velocity.x * blocksAmount.getValueAtLevel(level),
            damagee.velocity.y * blocksAmount.getValueAtLevel(level),
            damagee.velocity.z * blocksAmount.getValueAtLevel(level)
        )
    }

    override fun getName(): String {
        return "Knockback"
    }

    override fun getEnchantReferenceName(): String {
        return "Knockback"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Increases knockback taken by<br/>enemies by <white>{0} blocks</white>"
        )
        enchantLoreParser.setSingleVariable("3", "6", "9")
        return enchantLoreParser.parseForLevel(level)
    }

    override fun isDisabledOnPassiveWorld(): Boolean {
        return false
    }

    override fun getEnchantGroup(): EnchantGroup {
        return EnchantGroup.C
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