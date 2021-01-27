package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import org.bukkit.Sound
import java.util.*

class Gamble @Inject constructor(private val damageManager: DamageManager, playerDamageTrigger: PlayerDamageTrigger) :
    DamageTriggeredEnchant(
        arrayOf(playerDamageTrigger), arrayOf(damageManager)
    ) {
    private val damageAmount = EnchantProperty(2, 4, 6)
    private val random = Random()

    override val name: String get() = "Gamble"
    override val enchantReferenceName: String get() = "gamble"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = true
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val damager = data.damager
        val damagee = data.damagee
        val level = data.level
        val damage = damageAmount.getValueAtLevel(level)
        val chance = 0.5f
        val randomValue = random.nextFloat()
        if (randomValue >= chance) {
            damageManager.doTrueDamage(damagee, damage.toDouble(), damager)
            damager.playSound(damager.location, Sound.NOTE_PLING, 1f, 3f)
        } else {
            damageManager.doTrueDamage(damager, damage.toDouble(), damagee)
            damager.playSound(damager.location, Sound.NOTE_PLING, 1f, 3f)
            damagee.playSound(damager.location, Sound.NOTE_PLING, 1f, 1.5f)
        }
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "<light_purple>50% chance</light_purple> to deal <red>{0}</red> true<br/>damage to whoever you hit, or to<br/>yourself"
        )
        enchantLoreParser.setSingleVariable("1❤", "2❤", "3❤")
        return enchantLoreParser.parseForLevel(level)
    }
}