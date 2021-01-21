package com.thebluehats.server.game.managers.enchants

import org.bukkit.Material
import java.util.*

interface CustomEnchant {
    val name: String
    val enchantReferenceName: String
    val isDisabledOnPassiveWorld: Boolean
    val enchantGroup: EnchantGroup
    val isRareEnchant: Boolean
    val enchantItemTypes: Array<Material>
    fun getDescription(level: Int): ArrayList<String>
}