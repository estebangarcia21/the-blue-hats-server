package com.thebluehats.server.game.managers.combat

import org.bukkit.ChatColor

enum class CombatStatus(val formattedStatus: String) {
    COMBAT(ChatColor.RED.toString() + "Fighting"), IDLING(ChatColor.GREEN.toString() + "Idling");

}