package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import com.thebluehats.server.game.utils.EntityValidator
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import java.util.*
import javax.inject.Inject

class Executioner @Inject constructor(
    private val damageManager: DamageManager,
    playerDamageTrigger: PlayerDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf<DamageEnchantTrigger>(playerDamageTrigger), arrayOf<EntityValidator>(
        damageManager
    )
) {
    private val heartsToDie = EnchantProperty(3, 4, 4)
    override fun execute(data: DamageEventEnchantData) {
        val damagee = data.damagee
        val level = data.level
        val damager = data.damager
        if (damagee.health - damageManager.getFinalDamageFromEvent(data.event) / 2 <= heartsToDie.getValueAtLevel(level) && damagee.health > 0) {
            damagee.sendMessage(
                ChatColor.RED.toString() + ChatColor.BOLD + "EXECUTED!" + ChatColor.GRAY + " by " //                    + PermissionsManager.getInstance().getPlayerRank((Player) args[1]).getNameColor()
                        + damager.name
                        + damagee.name + ChatColor.GRAY + " (insta-kill below " + ChatColor.RED
                        + heartsToDie.getValueAtLevel(level) / 2 + "❤" + ChatColor.GRAY + ")"
            )
            damagee.world.playSound(damagee.location, Sound.VILLAGER_DEATH, 1f, 0.5f)

            // TODO Add name color in message
            // TODO Add particle
            damagee.health = 0.0

//            damageManager.safeSetPlayerHealth(damagee, 0);
        }
    }

    override fun getName(): String {
        return "Executioner"
    }

    override fun getEnchantReferenceName(): String {
        return "Executioner"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("Hitting an enemy below <red>{0}</red> instantly kills them")
        enchantLoreParser.setSingleVariable("1.5❤", "2❤", "2❤")
        return null
    }

    override fun isDisabledOnPassiveWorld(): Boolean {
        return false
    }

    override fun getEnchantGroup(): EnchantGroup {
        return EnchantGroup.B
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