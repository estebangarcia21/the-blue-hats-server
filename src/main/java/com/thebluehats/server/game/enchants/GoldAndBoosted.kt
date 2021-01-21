package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.CalculationMode
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import net.minecraft.server.v1_8_R3.EntityLiving
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import java.util.*

class GoldAndBoosted @Inject constructor(
    private val damageManager: DamageManager,
    playerDamageTrigger: PlayerDamageTrigger
) : DamageTriggeredEnchant(
    arrayOf(playerDamageTrigger), arrayOf(damageManager)
) {
    private val percentDamageIncrease = EnchantProperty(0.05f, 0.09f, 0.15f)

    override val name: String get() = "Gold and Boosted"
    override val enchantReferenceName: String get() = "gold-and-boosted"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.A
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.GOLD_SWORD)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val event = data.event
        val damager = data.damager
        val level = data.level
        val nmsPlayer: EntityLiving = (damager as CraftPlayer).handle

        if (nmsPlayer.absorptionHearts <= 1) {
            damageManager.addDamage(
                event,
                percentDamageIncrease.getValueAtLevel(level).toDouble(),
                CalculationMode.ADDITIVE
            )
        }
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser(
            "Deal <red>+{0}</red> damage when you have<br/>absorption hearts"
        )
        enchantLoreParser.setSingleVariable("5%", "9%", "15%")
        return enchantLoreParser.parseForLevel(level)
    }
}