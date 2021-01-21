package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.enchants.CustomEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import java.util.*

class Mirror : CustomEnchant {
    override fun getName(): String {
        return "Mirror"
    }

    override fun getEnchantReferenceName(): String {
        return "Mirror"
    }

    override fun getDescription(level: Int): ArrayList<String> {
        val enchantLoreParser = EnchantLoreParser("")
        enchantLoreParser.addTextIf(level == 1, "You are immune to true damage")
        enchantLoreParser.addTextIf(
            level != 1,
            "You do not take true damage and<br/>instead reflect <yellow>{0}</yellow> of it to<br/>your attacker"
        )
        enchantLoreParser.setSingleVariable("", "25%", "50%")
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
        return arrayOf(Material.LEATHER_LEGGINGS)
    }
}