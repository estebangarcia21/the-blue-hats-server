package com.thebluehats.server.game.utils

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

val PlayerInventory.armor: Array<ItemStack?>
    get() = arrayOf(helmet, chestplate, leggings, boots)
