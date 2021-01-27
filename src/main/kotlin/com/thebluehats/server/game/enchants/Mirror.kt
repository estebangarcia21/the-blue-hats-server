package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.enchants.CustomEnchant
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import com.thebluehats.server.game.utils.EnchantLoreParser
import org.bukkit.Material
import java.util.*

class Mirror : CustomEnchant {
    override val name: String get() = "Mirror"
    override val enchantReferenceName: String get() = "mirror"
    override val isDisabledOnPassiveWorld: Boolean get() = false
    override val enchantGroup: EnchantGroup get() = EnchantGroup.B
    override val isRareEnchant: Boolean get() = false
    override val enchantItemTypes: Array<Material> get() = arrayOf(Material.LEATHER_LEGGINGS)

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
}