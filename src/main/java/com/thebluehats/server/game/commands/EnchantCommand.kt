package com.thebluehats.server.game.commands

import com.google.inject.Inject
import com.thebluehats.server.game.managers.enchants.CustomEnchant
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils
import org.apache.commons.lang.StringUtils
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player

class EnchantCommand @Inject constructor(
    private val customEnchantManager: CustomEnchantManager,
    private val customEnchantUtils: CustomEnchantUtils
) : GameCommand() {
    override val commandNames: Array<String>
        get() = arrayOf("pitenchant")

    override fun getUsageMessage(cmd: String?): String? {
        return formatStandardUsageMessage(cmd!!, "Enchants an item with a Custom Enchant.", "enchantName", "level")
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
        if (args.size < 2) {
            player.sendMessage(formatStandardErrorMessage("You did not specify a level!"))
            return
        }
        if (!StringUtils.isNumeric(args[1])) {
            player.sendMessage(formatStandardErrorMessage("The level you entered is not a number!"))
            return
        }
        if (customEnchantUtils.itemHasEnchant(customEnchant, item)) {
            player.sendMessage(formatStandardErrorMessage("This item already contains that enchant!"))
            return
        }
        if (!customEnchantUtils.isCompatibleWith(customEnchant, item.type)) {
            player.sendMessage(formatStandardErrorMessage("You can not enchant this that on this item!"))
            return
        }
        val level = args[1].toInt()
        if (level > 3 || level < 1) {
            player.sendMessage(formatStandardErrorMessage("The enchant level can only be 1, 2, or 3!"))
            return
        }
        val numberOfEnchants = customEnchantManager.getItemEnchants(item).size
        if (numberOfEnchants >= 3) {
            player.sendMessage(
                formatStandardErrorMessage("You can only have a maximum of 3 enchants in this world!")
            )
            return
        }
        val tokens = customEnchantManager.getTokensOnItem(item) + level
        if (tokens > 8) {
            player.sendMessage("You can only have a miximum of 8 tokens in this world!")
            return
        }
        var rareTokens = 0
        var rareEnchantCount = 0
        val itemEnchants = customEnchantManager.getItemEnchants(item)
        for ((key, value) in itemEnchants) {
            if (key.isRareEnchant) {
                rareTokens += value
                rareEnchantCount++
            }
        }
        if (customEnchant.isRareEnchant) {
            rareTokens += level
            rareEnchantCount++
        }
        if (rareEnchantCount > 2) {
            player.sendMessage(
                formatStandardErrorMessage("You can only have 2 rare enchants on an item in this world!")
            )
            return
        }
        if (rareTokens > 4) {
            player.sendMessage(
                formatStandardErrorMessage(
                    "You can only have a maximum of 4 tokens for rare enchants in this world!"
                )
            )
            return
        }
        customEnchantManager.addEnchant(item, level, false, customEnchant)
        player.sendMessage(
            ChatColor.DARK_PURPLE.toString() + "Success!" + ChatColor.RED + " You applied the enchantment successfully!"
        )
        player.updateInventory()
    }
}