package com.thebluehats.server.game.commands

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GiveObsidianCommand : GameCommand() {
    override val commandNames: Array<String>
        get() = arrayOf("giveobsidian")

    override fun getUsageMessage(cmd: String?): String? {
        return formatStandardUsageMessage(cmd!!, "Gives a stack of obsidian.")
    }

    override fun runCommand(player: Player, cmd: String?, args: Array<String>) {
        val obsidian = ItemStack(Material.OBSIDIAN, 64)
        player.inventory.addItem(obsidian)
    }
}