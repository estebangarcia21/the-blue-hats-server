package com.thebluehats.server.game.commands

import com.google.inject.Inject
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager
import org.apache.commons.lang.StringUtils
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class MysticEnchantsCommand @Inject constructor(private val customEnchantManager: CustomEnchantManager) :
    GameCommand() {
    override val commandNames: Array<String>
        get() = arrayOf("pitenchants")

    override fun getUsageMessage(cmd: String?): String? {
        return formatStandardUsageMessage(
            cmd!!, "Lists the available Custom Enchants and their enchant names.",
            "pagenumber"
        )
    }

    override fun runCommand(player: Player, cmd: String?, args: Array<String>) {
        val pages = customEnchantManager.enchants.size / 9
        val page: Int
        val pageErrorNumber =
            ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Please specify a correct page number!"
        page = if (args.size == 1) {
            if (!StringUtils.isNumeric(args[0])) {
                player.sendMessage(pageErrorNumber)
                return
            }
            args[0].toInt()
        } else {
            player.sendMessage(getUsageMessage(cmd))
            return
        }
        if (page <= 0 || page > pages + 1) {
            player.sendMessage(pageErrorNumber)
            return
        }
        player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Mystic Enchants (" + page + "/" + (pages + 1) + ")")
        for (i in 0..8) {
            val index = i + (page - 1) * 9
            if (index > customEnchantManager.enchants.size - 1) {
                player.sendMessage(" ")
                continue
            }
            player.sendMessage(
                ChatColor.GRAY.toString() + "■ " + ChatColor.RED
                        + customEnchantManager.enchants[index].name + ChatColor.GOLD + " ▶ "
                        + ChatColor.YELLOW + customEnchantManager.enchants[index].enchantReferenceName
            )
        }
    }
}