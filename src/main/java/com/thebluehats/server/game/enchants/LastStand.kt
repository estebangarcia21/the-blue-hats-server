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

    override val name: String get() = "Last Stand"
    override val enchantReferenceName: String get() = "Laststand"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.LEATHER_LEGGINGS)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGEE

    override fun execute(data: DamageEventEnchantData) {
        val damagee = data.damagee
        if (damagee.health < 10) damagee.addPotionEffect(
            PotionEffect(
                PotionEffectType.DAMAGE_RESISTANCE, 80,
                resistanceAmplifier.getValueAtLevel(data.level)
            ), true
        )
    }
    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Gain <blue>Resistance {0}</blue> (4<br/>seconds) when reaching <red>3❤</red>"
        )
        enchantLoreParser.setSingleVariable("I", "II", "III")
        return enchantLoreParser.parseForLevel(level)
    }
}