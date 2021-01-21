package com.thebluehats.server.game.managers.enchants

import com.google.common.collect.ImmutableMap
import com.thebluehats.server.game.utils.PantsData
import com.thebluehats.server.game.utils.PantsData.FreshPantsColor
import com.thebluehats.server.game.utils.Registerer
import com.thebluehats.server.game.utils.RomanNumeralConverter
import com.thebluehats.server.game.utils.SortCustomEnchantByName
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

class CustomEnchantManager @Inject constructor(
    private val plugin: JavaPlugin, private val romanNumeralConverter: RomanNumeralConverter,
    private val pantsData: PantsData, private val customEnchantUtils: CustomEnchantUtils
) : Registerer<CustomEnchant?> {
    val enchants = ArrayList<CustomEnchant>()
    private val sortCustomEnchantByName = SortCustomEnchantByName()
    private val tierColors = ImmutableMap.builder<Int, ChatColor>()
        .put(1, ChatColor.GREEN).put(2, ChatColor.YELLOW).put(3, ChatColor.RED).build()

    override fun register(enchants: Array<CustomEnchant>) {
        for (enchant in enchants) {
            if (enchant is Listener) {
                plugin.server.pluginManager.registerEvents(enchant as Listener, plugin)
            }
            this.enchants.add(enchant)
        }
        this.enchants.sort(sortCustomEnchantByName)
    }

    fun addEnchant(item: ItemStack, level: Int, tierUp: Boolean, enchant: CustomEnchant) {
        val description = enchant.getDescription(level)
        val itemMeta = item.itemMeta
        val itemTypeName = item.type.toString()
        val keys = arrayOf("LEGGINGS", "SWORD", "BOW")
        var itemKey: String? = null
        var properlyCasedKey: String? = null
        val enchantName = enchant.name
        val isRareEnchant = enchant.isRareEnchant
        for (key in keys) {
            if (itemTypeName.contains(key)) {
                itemKey = key
                properlyCasedKey = key[0].toString() + key.substring(1).toLowerCase()
                break
            }
        }
        var textColor: String? = null
        val isFreshItem = isFreshItem(item)
        if (itemKey == null || itemMeta == null) return
        if (itemKey == "LEGGINGS") {
            val pantsData = pantsData.data
            textColor = if (isFreshItem) {
                val freshPantsColor = FreshPantsColor
                    .valueOf(ChatColor.stripColor(itemMeta.displayName.split(" ").toTypedArray()[1]).toUpperCase())
                pantsData[freshPantsColor]!!.textColor.toString()
            } else {
                itemMeta.displayName.substring(0, 2)
            }
        }
        if (isFreshItem) {
            if (itemKey == "LEGGINGS") {
                itemMeta.displayName = textColor + "Tier I Pants"
                itemMeta.lore = finalizePantsLore(enchantName, isRareEnchant, level, description, textColor)
            } else {
                itemMeta.displayName = tierColors[1].toString() + "Tier I " + properlyCasedKey
                itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 2, false)
                itemMeta.lore = finalizeHandheldLore(enchantName, isRareEnchant, level, description)
            }
            item.itemMeta = itemMeta
            return
        }
        if (itemKey == "LEGGINGS") {
            if (tierUp) itemMeta.displayName = upgradeTier(itemMeta.displayName, textColor)
            val lore = itemMeta.lore
            trimPantsLoreEnding(lore)
            lore.add("")
            lore.add(formatEnchantName(enchantName, isRareEnchant, level))
            lore.addAll(description!!)
            lore.add("")
            lore.add(textColor + "As strong as iron")
            itemMeta.lore = lore
        } else {
            if (tierUp) itemMeta.displayName = upgradeTier(itemMeta.displayName)
            val lore = itemMeta.lore ?: return
            lore.add("")
            lore.add(formatEnchantName(enchantName, isRareEnchant, level))
            lore.addAll(description!!)
            itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 2, false)
            itemMeta.lore = lore
        }
        item.itemMeta = itemMeta
    }

    private fun upgradeTier(displayName: String): String {
        val displayNameTokens = displayName.split(" ").toTypedArray()
        val tier = romanNumeralConverter.convertRomanNumeralToInteger(
            ChatColor.stripColor(
                displayNameTokens[1]
            )
        )
        val itemName = ChatColor.stripColor(displayNameTokens[2])
        val nextTier = tier + 1
        return (tierColors[nextTier].toString() + "Tier " + romanNumeralConverter.convertToRomanNumeral(nextTier) + " "
                + itemName)
    }

    private fun upgradeTier(displayName: String, textColor: String?): String {
        val displayNameTokens = displayName.split(" ").toTypedArray()
        val tier = romanNumeralConverter.convertRomanNumeralToInteger(
            ChatColor.stripColor(
                displayNameTokens[1]
            )
        )
        val itemName = ChatColor.stripColor(displayNameTokens[2])
        return textColor + "Tier " + romanNumeralConverter.convertToRomanNumeral(tier + 1) + " " + itemName
    }

    private fun finalizePantsLore(
        name: String?, isRare: Boolean, level: Int, description: ArrayList<String?>?,
        textColor: String?
    ): ArrayList<String?> {
        val finalLore = ArrayList<String?>()
        finalLore.add(ChatColor.GRAY.toString() + "Lives: " + ChatColor.GREEN + "69" + ChatColor.GRAY + "/420")
        finalLore.add("")
        finalLore.add(formatEnchantName(name, isRare, level))
        finalLore.addAll(description!!)
        finalLore.add("")
        finalLore.add(textColor + "As strong as iron")
        return finalLore
    }

    private fun finalizeHandheldLore(
        name: String?, isRare: Boolean, level: Int,
        description: ArrayList<String?>?
    ): ArrayList<String?> {
        val finalLore = ArrayList<String?>()
        finalLore.add(ChatColor.GRAY.toString() + "Lives: " + ChatColor.GREEN + "69" + ChatColor.GRAY + "/420")
        finalLore.add("")
        finalLore.add(formatEnchantName(name, isRare, level))
        finalLore.addAll(description!!)
        return finalLore
    }

    private fun formatEnchantName(name: String?, isRare: Boolean, level: Int): String {
        return ((if (isRare) ChatColor.LIGHT_PURPLE.toString() + "RARE! " else "") + ChatColor.BLUE + name
                + if (level != 1) " " + romanNumeralConverter.convertToRomanNumeral(level) else "")
    }

    fun isFreshItem(item: ItemStack): Boolean {
        val p = Pattern.compile("(Mystic Bow|Mystic Sword|Fresh)")
        val m = p.matcher(item.itemMeta.displayName)
        return m.find()
    }

    private fun trimPantsLoreEnding(lore: List<String?>) {
        lore.removeAt(lore.size - 1)
        lore.removeAt(lore.size - 1)
    }

    fun getItemLives(item: ItemStack): Int {
        return getItemLivesToken(item, 0)
    }

    fun getMaximumItemLives(item: ItemStack): Int {
        return getItemLivesToken(item, 1)
    }

    private fun getItemLivesToken(item: ItemStack, index: Int): Int {
        if (item.itemMeta.lore == null) return 0
        val displayNameTokens = ArrayList(
            Arrays.asList(*ChatColor.stripColor(item.itemMeta.displayName).split(" ").toTypedArray())
        )
        if (displayNameTokens.contains("Fresh") || displayNameTokens.contains("Mystic")) return 0
        val lore = item.itemMeta.lore
        val livesLine = lore[0]
        return ChatColor.stripColor(livesLine.split(" ").toTypedArray()[1]).split("/").toTypedArray()[index].toInt()
    }

    fun setItemLives(item: ItemStack, value: Int) {
        if (item.itemMeta.lore == null) return
        val lore = item.itemMeta.lore
        lore[0] =
            (ChatColor.GRAY.toString() + "Lives: " + (if (value > 3) ChatColor.GREEN else ChatColor.RED) + value + ChatColor.GRAY
                    + "/" + getMaximumItemLives(item))
        val meta = item.itemMeta
        meta.lore = lore
        item.itemMeta = meta
    }

    fun setMaximumItemLives(item: ItemStack, value: Int) {
        if (item.itemMeta.lore == null) return
        var lives = getItemLives(item)
        if (lives > value) lives = value
        val lore = item.itemMeta.lore
        lore[0] =
            (ChatColor.GRAY.toString() + "Lives: " + (if (value > 3) ChatColor.GREEN else ChatColor.RED) + lives + ChatColor.GRAY
                    + "/" + value)
        val meta = item.itemMeta
        meta.lore = lore
        item.itemMeta = meta
    }

    fun removeEnchant(item: ItemStack, enchant: CustomEnchant) {
        val itemMeta = item.itemMeta
        val lore = itemMeta.lore ?: return
        for (i in lore.indices) {
            val line = lore[i]
            if (line.contains(enchant.name)) {
                while (lore[i] != "") {
                    if (lore.size == i + 1) {
                        lore.removeAt(i)
                        break
                    }
                    lore.removeAt(i)
                }
                lore.removeAt(i - 1)
                break
            }
        }
        itemMeta.lore = lore
        item.itemMeta = itemMeta
    }

    fun getItemEnchants(item: ItemStack): HashMap<CustomEnchant, Int> {
        val enchantsToLevels = HashMap<CustomEnchant, Int>()
        val lore = item.itemMeta.lore ?: return enchantsToLevels
        for (enchant in enchants) {
            if (customEnchantUtils.itemHasEnchant(enchant, item)) {
                enchantsToLevels[enchant] = customEnchantUtils.getEnchantLevel(enchant, item)
            }
        }
        return enchantsToLevels
    }

    fun getTokensOnItem(item: ItemStack): Int {
        if (item.type == Material.AIR || item.itemMeta.lore == null) return 0
        var tokens = 0
        for ((_, value) in getItemEnchants(item)) {
            tokens += value
        }
        return tokens
    }

    fun getRawItemEnchants(item: ItemStack?): List<CustomEnchant> {
        if (item == null) return ArrayList()
        val enchants = ArrayList<CustomEnchant>()
        for (enchant in enchants) {
            if (customEnchantUtils.itemHasEnchant(enchant, item)) {
                enchants.add(enchant)
            }
        }
        return enchants
    }

    fun getItemTier(item: ItemStack): Int {
        val meta = item.itemMeta
        val tokens = ArrayList(
            Arrays.asList(*ChatColor.stripColor(meta.displayName).split(" ").toTypedArray())
        )
        if (tokens.contains("I")) {
            return 1
        } else if (tokens.contains("II")) {
            return 2
        } else if (tokens.contains("III")) {
            return 3
        } else if (tokens.contains("Fresh") || tokens.contains("Mystic")) {
            return 0
        }
        return -1
    }
}