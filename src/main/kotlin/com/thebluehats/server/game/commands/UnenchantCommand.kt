package com.thebluehats.server.game.commands

import com.google.inject.Inject
import com.thebluehats.server.game.managers.enchants.CustomEnchant
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player

class UnenchantCommand @Inject constructor(
    private val customEnchantManager: CustomEnchantManager,
    private val customEnchantUtils: CustomEnchantUtils
) : GameCommand() {
    override val commandNames: Array<String>
        get() = arrayOf("unenchant")

    override fun getUsageMessage(cmd: String?): String? {
        return formatStandardUsageMessage(cmd!!, "Unenchants an enchant from the item you are holding.", "enchantName")
    }

    override fun runCommand(player: Player, cmd: String?, args: Array<String>) {
        var customEnchant: CustomEnchant? = null
        for (enchant in customEnchantManager.enchants) {
            if (enchant.enchantReferenceName.equals(args[0], ignoreCase = true)) {
                customEnchant = enchant
            }
        }
        if (customEnchant == null) {
            player.sendMessage(formatStandardErrorMessage("This enchant does not exist!"))
            return
        }
        val item = player.inventory.itemInHand
        if (item.type == Material.AIR) {
            player.sendMessage(formatStandardErrorMessage("You are not holding anything!"))
            return
        }
        if (args.size > 1) {
            player.sendMessage(formatStandardErrorMessage("Too many arguments"))
            return
        }
        if (item.type != Material.LEATHER_LEGGINGS && item.type != Material.GOLD_SWORD && item.type != Material.BOW) {
            player.sendMessage(formatStandardErrorMessage("You can not unenchant this item!"))
            return
        }
        if (!customEnchantUtils.itemHasEnchant(customEnchant, item)) {
            player.sendMessage(ChatColor.DARK_PURPLE.toString() + "Error!" + ChatColor.RED + " This item does not have the specified enchant!")
            return
        }
        customEnchantManager.removeEnchant(item, customEnchant)
        player.sendMessage(ChatColor.DARK_PURPLE.toString() + "Success!" + ChatColor.RED + " You unenchanted the enchant successfully!")
    }
}