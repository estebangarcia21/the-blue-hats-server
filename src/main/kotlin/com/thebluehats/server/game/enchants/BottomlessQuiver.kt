package com.thebluehats.server.game.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.managers.enchants.EnchantProperty
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

class BottomlessQuiver @Inject constructor(arrowDamageTrigger: ArrowDamageTrigger) :
    DamageTriggeredEnchant(arrayOf(arrowDamageTrigger)) {
    private val arrowAmount = EnchantProperty(1, 3, 8)

    override val name: String get() = "Bottomless Quiver"
    override val enchantReferenceName: String get() = "bottomless-quiver"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.A
    override val isRareEnchant: Boolean get() = true
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.BOW)
    override val enchantHolder: EnchantHolder get() = EnchantHolder.DAMAGER

    override fun execute(data: DamageEventEnchantData) {
        val damager = data.damager
        val level = data.level
        val arrows = ItemStack(Material.ARROW, arrowAmount.getValueAtLevel(level))

        damager.inventory.addItem(arrows)
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser()

        enchantLoreParser.addTextIf(level == 1, "Get <white>{0} arrow</white> on arrow hit")
        enchantLoreParser.addTextIf(level != 1, "Get <white>{0} arrows</white> on arrow hit")

        enchantLoreParser.setSingleVariable("1", "3", "8")

        return enchantLoreParser.parseForLevel(level)
    }
}