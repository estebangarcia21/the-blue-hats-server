package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.BowManager
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.event.entity.EntityShootBowEvent
import com.thebluehats.server.game.utils.EnchantLoreParser
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import org.bukkit.Material
import org.bukkit.event.EventHandler
import java.util.ArrayList
import javax.inject.Inject

class Wasp @Inject constructor(private val bowManager: BowManager, arrowDamageTrigger: ArrowDamageTrigger) : DamageTriggeredEnchant(arrayOf<DamageEnchantTrigger>(arrowDamageTrigger)) {
    private val weaknessDuration = EnchantProperty(6, 11, 16)
    private val weaknessAmplifier = EnchantProperty(1, 2, 3)

    override fun execute(data: DamageEventEnchantData) {
        val level = data.level

        data.damagee.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS,
                weaknessDuration.getValueAtLevel(level) * 20, weaknessAmplifier.getValueAtLevel(level)), true)
    }

    @EventHandler
    fun onArrowShootEvent(event: EntityShootBowEvent?) {
        bowManager.onArrowShoot(event)
    }

    override fun getName(): String {
        return "Wasp"
    }

    override fun getEnchantReferenceName(): String {
        return "Wasp"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("Apply <red>Weakness {0}</red> ({1}s) on hit")

        val variables: Array<Array<String>?> = arrayOfNulls(2)
        variables[0] = arrayOf("II", "III", "IV")
        variables[1] = arrayOf("6", "11", "16")

        enchantLoreParser.setVariables(variables)

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
        return arrayOf(Material.BOW)
    }

    override fun getEnchantHolder(): EnchantHolder {
        return EnchantHolder.DAMAGER
    }
}