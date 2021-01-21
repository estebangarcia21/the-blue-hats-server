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
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class SpeedyHit @Inject constructor(private val timer: Timer<UUID>, playerDamageTrigger: PlayerDamageTrigger) :
    DamageTriggeredEnchant(arrayOf<DamageEnchantTrigger>(playerDamageTrigger)) {
    private val speedDuration = EnchantProperty(5, 7, 9)
    private val cooldownTime = EnchantProperty(3, 2, 1)
    override fun execute(data: DamageEventEnchantData) {
        val damager = data.damager
        val playerUuid = damager.uniqueId
        val level = data.level
        if (!timer.isRunning(playerUuid)) {
            damager.addPotionEffect(
                PotionEffect(PotionEffectType.SPEED, speedDuration.getValueAtLevel(level) * 20, 0, true)
            )
        }
        timer.start(playerUuid, (cooldownTime.getValueAtLevel(level) * 20).toLong(), false)
    }

    override fun getName(): String {
        return "Speedy Hit"
    }

    override fun getEnchantReferenceName(): String {
        return "Speedyhit"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Gain Speed I for <yellow>{0}s</yellow> on hit({1}s<br/>cooldown)"
        )
        val variables: Array<Array<String>> = arrayOfNulls(2)
        variables[0] = arrayOf("5", "7", "9")
        variables[1] = arrayOf("3", "2", "1")
        enchantLoreParser.setVariables(variables)
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
        return true
    }

    override fun getEnchantItemTypes(): Array<Material> {
        return arrayOf(Material.GOLD_SWORD)
    }

    override fun getEnchantHolder(): EnchantHolder {
        return EnchantHolder.DAMAGER
    }
}