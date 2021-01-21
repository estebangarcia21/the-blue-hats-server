package com.thebluehats.server.game.commands

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GiveArrowCommand : GameCommand() {
    override val commandNames: Array<String>
        get() = arrayOf("givearrows")

    override fun getUsageMessage(cmd: String?): String? {
        return formatStandardUsageMessage(cmd!!, "Gives a stack of arrows.")
    }

    override fun runCommand(player: Player, cmd: String?, args: Array<String>) {
        val arrows = ItemStack(Material.ARROW, 64)
        player.inventory.addItem(arrows)
    }
}