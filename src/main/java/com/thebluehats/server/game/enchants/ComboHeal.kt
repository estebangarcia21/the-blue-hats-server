package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.HitCounter
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import java.util.*

class ComboHeal @Inject constructor(private val hitCounter: HitCounter, playerDamageTrigger: PlayerDamageTrigger) :
    DamageTriggeredEnchant(arrayOf<DamageEnchantTrigger>(playerDamageTrigger)) {
    private val healingAmount = EnchantProperty(.80f, 1.6f, 2.4f)
    override fun execute(data: DamageEventEnchantData) {
        val damager = data.damager
        hitCounter.addOne(damager)
        if (hitCounter.hasHits(damager, 4)) {
            val nmsPlayer = (damager as CraftPlayer).handle
            damager.playSound(damager.getLocation(), Sound.DONKEY_HIT, 1f, 0.5f)
            nmsPlayer.absorptionHearts =
                Math.min(nmsPlayer.absorptionHearts + healingAmount.getValueAtLevel(data.level), 8f)
        }
    }

    override fun getName(): String {
        return "Combo: Heal"
    }

    override fun getEnchantReferenceName(): String {
        return "Comboheal"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Every <yellow>fourth</yellow> strike heals<br/><red>{0}❤</red> and grants <gold>{0}❤</gold><br/>absorption"
        )
        enchantLoreParser.setSingleVariable("0.4", "0.8", "1.2")
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
        return EnchantHolder.DAMAGER
    }
}