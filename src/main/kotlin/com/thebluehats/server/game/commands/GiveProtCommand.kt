package com.thebluehats.server.game.commands

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GiveProtCommand : GameCommand() {
    override val commandNames: Array<String>
        get() = arrayOf("giveprot")

    override fun getUsageMessage(cmd: String?): String? {
        return formatStandardUsageMessage(cmd!!, "Gives a prot 1 set.")
    }

    override fun runCommand(player: Player, cmd: String?, args: Array<String>) {
        val helmet = ItemStack(Material.DIAMOND_HELMET, 1)
        addProtectionOne(helmet)
        val chestplate = ItemStack(Material.DIAMOND_CHESTPLATE, 1)
        addProtectionOne(chestplate)
        val leggings = ItemStack(Material.DIAMOND_LEGGINGS, 1)
        addProtectionOne(leggings)
        val boots = ItemStack(Material.DIAMOND_BOOTS, 1)
        addProtectionOne(boots)
        val sword = ItemStack(Material.DIAMOND_SWORD, 1)
        val swordMeta = sword.itemMeta
        swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true)
        swordMeta.spigot().isUnbreakable = true
        sword.itemMeta = swordMeta
        player.inventory.addItem(helmet, chestplate, leggings, boots, sword)
    }

    private fun addProtectionOne(item: ItemStack) {
        val meta = item.itemMeta
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true)
        meta.spigot().isUnbreakable = true
        item.itemMeta = meta
    }
}