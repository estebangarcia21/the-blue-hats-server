package com.thebluehats.server.game.commands

import com.thebluehats.server.game.utils.LoreParser
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GiveBreadCommand : GameCommand() {
    override val commandNames: Array<String>
        get() = arrayOf("givebread")

    override fun getUsageMessage(cmd: String?): String? {
        return formatStandardUsageMessage(cmd!!, "Gives a stack of Yummy Bread.")
    }

    override fun runCommand(player: Player, cmd: String?, args: Array<String>) {
        val bread = ItemStack(Material.BREAD, 64)
        val meta = bread.itemMeta
        val breadLore = LoreParser("Heals <red>4❤</red><br/>Grants <gold>1❤</gold>").parse()
        meta.displayName = ChatColor.GOLD.toString() + "Yummy Bread"
        meta.lore = breadLore
        bread.itemMeta = meta
        player.inventory.addItem(bread)
    }
}