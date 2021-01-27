package com.thebluehats.server.game.commands

import org.bukkit.ChatColor
import org.bukkit.entity.Player

class AboutCommand : GameCommand() {
    override val commandNames: Array<String>
        get() = arrayOf("about")

    override fun getUsageMessage(cmd: String?): String? {
        return null
    }

    override fun runCommand(player: Player, cmd: String?, args: Array<String>) {
        val pluginName = "The Blue Hats Server"
        val version = "v1.0"
        val creatorDiscord = "Stevemmmmm#8894"

        player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + pluginName + " " + version)
        player.sendMessage(ChatColor.YELLOW.toString() + "by " + ChatColor.RED + "Stevemmmmm")

        for (i in 0..6) player.sendMessage(" ")

        player.sendMessage(
            ChatColor.DARK_PURPLE.toString() + ChatColor.UNDERLINE + "Discord" + ChatColor.YELLOW + " ▶ "
                    + ChatColor.BLUE + creatorDiscord
        )
    }
}