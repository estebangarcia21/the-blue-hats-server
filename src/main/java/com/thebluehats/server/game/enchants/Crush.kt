package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.Timer
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class Crush @Inject constructor(private val timer: Timer<Player>, playerDamageTrigger: PlayerDamageTrigger) :
    DamageTriggeredEnchant(arrayOf<DamageEnchantTrigger>(playerDamageTrigger)) {
    private val weaknessAmplifier = EnchantProperty(4, 5, 6)
    private val weaknessDuration = EnchantProperty(4, 8, 10)
    override fun execute(data: DamageEventEnchantData) {
        val damager = data.damager
        val damagee = data.damagee
        val level = data.level
        if (!timer.isRunning(damager)) {
            damagee.player.addPotionEffect(
                PotionEffect(
                    PotionEffectType.WEAKNESS,
                    weaknessDuration.getValueAtLevel(level), weaknessAmplifier.getValueAtLevel(level)
                ), true
            )
        }
        timer.start(damager, 40, false)
    }

    override fun getName(): String {
        return "Crush"
    }

    override fun getEnchantReferenceName(): String {
        return "Crush"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Strikes apply <red>Weakness {0}</red><br/>(lasts, {1}s, 2s cooldown)"
        )
        val variables: Array<Array<String>> = arrayOfNulls(2)
        variables[0] = arrayOf("V", "VI", "VII")
        variables[1] = arrayOf("0.2", "0.4", "0.5")
        enchantLoreParser.setVariables(variables)
        return enchantLoreParser.parseForLevel(level)
    }

    override fun isDisabledOnPassiveWorld(): Boolean {
        return false
    }

    override fun getEnchantGroup(): EnchantGroup {
        return EnchantGroup.A
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