package com.thebluehats.server.game.commands

import com.google.inject.Inject
import com.thebluehats.server.game.utils.BasicLoreParser
import com.thebluehats.server.game.utils.PantsData
import com.thebluehats.server.game.utils.PantsData.FreshPantsColor
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import java.util.*

class GiveFreshItemCommand @Inject constructor(private val pantsData: PantsData) : GameCommand() {
    private val freshPantsColors = ArrayList<String>()
    private val handheldFreshItems = ArrayList<String>()

    private enum class HandheldFreshItem {
        SWORD, BOW
    }

    override val commandNames: Array<String>
        get() = arrayOf("givefreshitem")

    override fun getUsageMessage(cmd: String?): String {
        return formatStandardUsageMessage(
            cmd!!,
            "Gives a fresh item. Must be: sword, bow, or any pants color. (Ex: /givefreshitem red)", "type"
        )
    }

    override fun runCommand(player: Player, cmd: String?, args: Array<String>) {
        val freshItemName = args[0].toUpperCase()
        if (handheldFreshItems.contains(freshItemName)) {
            giveFreshHandheldItem(player, HandheldFreshItem.valueOf(freshItemName))
        } else if (freshPantsColors.contains(freshItemName)) {
            giveFreshPants(player, FreshPantsColor.valueOf(freshItemName))
        } else {
            player.sendMessage(getUsageMessage(cmd))
        }
    }

    private fun giveFreshHandheldItem(player: Player, handheldFreshItem: HandheldFreshItem) {
        val freshItemLore = BasicLoreParser("Kept on death<br/><br/>Used in the mystic well").parse()
        val freshItem = if (handheldFreshItem == HandheldFreshItem.SWORD) ItemStack(Material.GOLD_SWORD) else ItemStack(
            Material.BOW
        )
        val meta = freshItem.itemMeta
        val handheldFreshItemString = handheldFreshItem.toString().toLowerCase()
        val handheldFreshItemName = (handheldFreshItemString.substring(0, 1).toUpperCase()
                + handheldFreshItemString.substring(1))
        meta.displayName = ChatColor.AQUA.toString() + "Mystic " + handheldFreshItemName
        meta.lore = freshItemLore
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
        meta.spigot().isUnbreakable = true
        applyFlags(meta)
        freshItem.itemMeta = meta
        player.inventory.addItem(freshItem)
        player.updateInventory()
    }

    private fun giveFreshPants(player: Player, pantsColor: FreshPantsColor) {
        val freshLeggings = ItemStack(Material.LEATHER_LEGGINGS)
        val freshPantsMeta = freshLeggings.itemMeta as LeatherArmorMeta
        val pantsColorName = pantsColor.toString().toLowerCase()
        val data = pantsData.data[pantsColor]
        val textColor = data!!.textColor
        val color = data.pantsColor

        freshPantsMeta.color = Color.fromRGB(color)
        freshPantsMeta.displayName = (textColor.toString() + "Fresh " + pantsColorName.substring(0, 1).toUpperCase()
                + pantsColorName.substring(1) + " Pants")
        applyFlags(freshPantsMeta)

        val loreParser = BasicLoreParser(
            "Kept on death<br/><br/>{0}Used in the mystic well{1}<br/>{0}Also, a fashion statement{1}"
        )

        val textColorName = textColor.name.toLowerCase()

        loreParser.setVariables(arrayOf("<$textColorName>", "</$textColorName>"))

        freshPantsMeta.lore = loreParser.parse()
        freshLeggings.itemMeta = freshPantsMeta

        player.inventory.addItem(freshLeggings)
    }

    private fun applyFlags(meta: ItemMeta) {
        meta.spigot().isUnbreakable = true
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS)
    }

    init {
        for (freshPantsColor in FreshPantsColor.values()) {
            freshPantsColors.add(freshPantsColor.toString())
        }
        for (handheldFreshItem in HandheldFreshItem.values()) {
            handheldFreshItems.add(handheldFreshItem.toString())
        }
    }
}