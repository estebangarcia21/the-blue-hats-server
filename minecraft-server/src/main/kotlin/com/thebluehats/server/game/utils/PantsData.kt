package com.thebluehats.server.game.utils

import com.google.common.collect.ImmutableMap
import org.bukkit.ChatColor

class PantsData {
    enum class FreshPantsColor {
        RED, GREEN, BLUE, YELLOW, ORANGE, DARK, SEWER, AQUA
    }

    class PantsDataValue(val pantsColor: Int, val textColor: ChatColor)

    val data = ImmutableMap
        .builder<FreshPantsColor, PantsDataValue>().put(FreshPantsColor.RED, PantsDataValue(0xFF5555, ChatColor.RED))
        .put(FreshPantsColor.GREEN, PantsDataValue(0x55FF55, ChatColor.GREEN))
        .put(FreshPantsColor.BLUE, PantsDataValue(0x5555FF, ChatColor.BLUE))
        .put(FreshPantsColor.YELLOW, PantsDataValue(0xFFFF55, ChatColor.YELLOW))
        .put(FreshPantsColor.ORANGE, PantsDataValue(0xFFAA00, ChatColor.GOLD))
        .put(FreshPantsColor.DARK, PantsDataValue(0x000000, ChatColor.DARK_PURPLE))
        .put(FreshPantsColor.SEWER, PantsDataValue(0x7DC383, ChatColor.DARK_AQUA))
        .put(FreshPantsColor.AQUA, PantsDataValue(0x55FFFF, ChatColor.DARK_AQUA)).build()
}