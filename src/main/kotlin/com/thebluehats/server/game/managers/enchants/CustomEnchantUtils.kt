package com.thebluehats.server.game.managers.enchants

import com.google.inject.Inject
import com.thebluehats.server.game.utils.RomanNumeralConverter
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class CustomEnchantUtils @Inject constructor(private val romanNumeralConverter: RomanNumeralConverter) {
    fun isCompatibleWith(enchant: CustomEnchant, material: Material): Boolean {
        for (mat in enchant.enchantItemTypes) {
            if (mat == material) {
                return true
            }
        }
        return false
    }

    fun getItemEnchantData(enchant: CustomEnchant, item: ItemStack?): ItemEnchantData {
        return ItemEnchantData(itemHasEnchant(enchant, item), getEnchantLevel(enchant, item))
    }

    fun itemHasEnchant(enchant: CustomEnchant, item: ItemStack?): Boolean {
        if (item == null || item.type == Material.AIR) return false
        val lore = item.itemMeta.lore ?: return false
        for (line in lore) {
            if (line.contains(enchant.name)) {
                return true
            }
        }
        return false
    }

    fun getEnchantLevel(enchant: CustomEnchant, item: ItemStack?): Int {
        if (item == null || item.type == Material.AIR) return 0
        val lore = item.itemMeta.lore ?: return 0
        for (line in lore) {
            if (line.contains(enchant.name)) {
                val strippedLine = ChatColor.stripColor(line)
                val lineTokens = strippedLine.split(" ").toTypedArray()
                val numeral = lineTokens[lineTokens.size - 1]
                val level = romanNumeralConverter.convertRomanNumeralToInteger(numeral)
                return if (level == -1) 1 else level
            }
        }
        return 0
    }

    class ItemEnchantData constructor(private val itemHasEnchant: Boolean, val enchantLevel: Int) {
        fun itemHasEnchant(): Boolean {
            return itemHasEnchant
        }
    }
}