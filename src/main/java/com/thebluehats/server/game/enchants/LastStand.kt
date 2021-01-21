package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class LastStand @Inject constructor(playerDamageTrigger: PlayerDamageTrigger, arrowDamageTrigger: ArrowDamageTrigger) :
    DamageTriggeredEnchant(arrayOf(playerDamageTrigger, arrowDamageTrigger)) {
    private val resistanceAmplifier = EnchantProperty(0, 1, 2)
    override fun execute(data: DamageEventEnchantData) {
        val damagee = data.damagee
        if (damagee.health < 10) damagee.addPotionEffect(
            PotionEffect(
                PotionEffectType.DAMAGE_RESISTANCE, 80,
                resistanceAmplifier.getValueAtLevel(data.level)
            ), true
        )
    }

    override fun getName(): String {
        return "Last Stand"
    }

    override fun getEnchantReferenceName(): String {
        return "Laststand"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Gain <blue>Resistance {0}</blue> (4<br/>seconds) when reaching <red>3❤</red>"
        )
        enchantLoreParser.setSingleVariable("I", "II", "III")
        return enchantLoreParser.parseForLevel(level)
    }

    override fun isDisabledOnPassiveWorld(): Boolean {
        return true
    }

    override fun getEnchantGroup(): EnchantGroup {
        return EnchantGroup.B
    }

    override fun isRareEnchant(): Boolean {
        return false
    }

    override fun getEnchantItemTypes(): Array<Material> {
        return arrayOf(Material.LEATHER_LEGGINGS)
    }

    override fun getEnchantHolder(): EnchantHolder {
        return EnchantHolder.DAMAGEE
    }
}