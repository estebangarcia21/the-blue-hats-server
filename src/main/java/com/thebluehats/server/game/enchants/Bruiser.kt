package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import com.thebluehats.server.game.utils.EntityValidator
import org.bukkit.Material
import java.util.*

class Bruiser @Inject constructor(
    private val damageManager: DamageManager,
    playerDamageTrigger: PlayerDamageTrigger,
    arrowDamageTrigger: ArrowDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf(playerDamageTrigger, arrowDamageTrigger), arrayOf<EntityValidator>(
        damageManager
    )
) {
    private val heartsReduced = EnchantProperty(1, 2, 4)
    override fun execute(data: DamageEventEnchantData) {
        if (data.damagee.isBlocking) {
            damageManager.addHeartDamageReduction(data.event, heartsReduced.getValueAtLevel(data.level))
        }
    }

    override fun getName(): String {
        return "Bruiser"
    }

    override fun getEnchantReferenceName(): String {
        return "Bruiser"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser =
            EnchantLoreParser("Blocking with your sword reduces<br/>damage received by <red>{0}</red>")
        enchantLoreParser.setSingleVariable("0.5❤", "1❤", "2❤")
        return enchantLoreParser.parseForLevel(level)
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
        return EnchantHolder.DAMAGEE
    }
}