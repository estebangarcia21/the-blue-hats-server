package com.thebluehats.server.game.managers.enchants

import com.thebluehats.server.game.utils.numeralToInteger
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class CustomEnchantUtils {
    fun isCompatibleWith(enchant: CustomEnchant, material: Material): Boolean {
        return enchant.enchantItemTypes.any { t -> t == material }
    }

    fun getItemEnchantData(enchant: CustomEnchant, item: ItemStack?): ItemEnchantData {
        return ItemEnchantData(itemHasEnchant(enchant, item), getEnchantLevel(enchant, item))
    }

    fun itemHasEnchant(enchant: CustomEnchant, item: ItemStack?): Boolean {
        if (item == null || item.type == Material.AIR) return false
        val lore = item.itemMeta.lore ?: return false

        return lore.any { l -> l.contains(enchant.name) }
    }

    fun getEnchantLevel(enchant: CustomEnchant, item: ItemStack?): Int {
        if (item == null || item.type == Material.AIR) return 0
        val lore = item.itemMeta.lore ?: return 0

        lore.forEach { l ->
            if (!l.contains(enchant.name)) return@forEach

            val tokens = ChatColor.stripColor(l).split(" ").toTypedArray()
            val numeral = tokens[tokens.size - 1]

            val level = numeral.numeralToInteger()

            return if (level == -1) 1 else level
        }

        return 0
    }

    class ItemEnchantData constructor(private val itemHasEnchant: Boolean, val enchantLevel: Int) {
        fun itemHasEnchant(): Boolean {
            return itemHasEnchant
        }
    }
}
