package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.HitCounter
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import com.thebluehats.server.game.utils.EntityValidator
import org.bukkit.ChatColor
import org.bukkit.Material
import java.util.*

class Perun @Inject constructor(
    private val damageManager: DamageManager,
    private val hitCounter: HitCounter,
    playerDamageTrigger: PlayerDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf(playerDamageTrigger), arrayOf(
        damageManager
    )
) {
    private val perunDamage = EnchantProperty(3, 4, 2)
    private val hitsNeeded = EnchantProperty(5, 4, 4)

    override val name: String get() = "Perun"
    override val enchantReferenceName: String get() = "Perun"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = true
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val damager = data.damager
        val damagee = data.damagee
        val level = data.level
        var damage = perunDamage.getValueAtLevel(level)
        hitCounter.addOne(damager)
        if (hitCounter.hasHits(damager, hitsNeeded.getValueAtLevel(level))) {
            if (level == 3) {
                if (damagee.inventory.boots != null) if (damagee.inventory.boots.type == Material.DIAMOND_BOOTS) damage += 2
                if (damagee.inventory.chestplate != null) if (damagee.inventory.chestplate.type == Material.DIAMOND_CHESTPLATE) damage += 2
                if (damagee.inventory.leggings != null) if (damagee.inventory.leggings.type == Material.DIAMOND_LEGGINGS) damage += 2
                if (damagee.inventory.helmet != null) if (damagee.inventory.helmet.type == Material.DIAMOND_HELMET) damage += 2
            }
            damager.world.strikeLightningEffect(damagee.location)
            damageManager.doTrueDamage(damagee, damage.toDouble(), damager)
        }
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("")
        val lastMessage = ChatColor.ITALIC.toString() + "Lightning deals true damage"
        enchantLoreParser.addTextIf(
            level == 2,
            "Every <yellow>fifth</yellow> hit strikes<br/><yellow>lightning</yellow> for <red>{0}❤</red><br/>$lastMessage"
        )
        enchantLoreParser.addTextIf(
            level == 2,
            "Every <yellow>fourth</yellow> hit strikes<br/><yellow>lightning</yellow> for <red>{0}❤</red><br/>$lastMessage"
        )
        enchantLoreParser.addTextIf(
            level == 3,
            "Every <yellow>fourth</yellow> hit strikes<br/><yellow>lightning</yellow> for <red>{0}❤</red> + " +
                "<red>1❤</red><br/>per <aqua>diamond piece</aqua> on your<br/>victim.<br/>"
                    + lastMessage
        )
        enchantLoreParser.setSingleVariable("1.5", "2", "1")
        return enchantLoreParser.parseForLevel(level)
    }
}