package com.thebluehats.server.game.other

import com.thebluehats.server.api.utils.DataLoader
import com.thebluehats.server.game.utils.PluginLifecycleListener
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import java.util.*

class EnderChest : Listener, PluginLifecycleListener, DataLoader {
    private val enderChests = HashMap<UUID, Inventory>()
    @EventHandler
    fun onEnderChestOpen(event: InventoryOpenEvent) {
        val player = event.player
        if (event.inventory.type == InventoryType.ENDER_CHEST) {
            event.isCancelled = true
            player.openInventory(enderChests.computeIfAbsent(player.uniqueId) { k: UUID? -> createInventory() })
        }
    }

    private fun createInventory(): Inventory {
        return Bukkit.createInventory(null, 54, ChatColor.GRAY.toString() + "Ender Chest")
    }

    override fun loadData() {}
    override fun saveData() {
//        Gson gson = new Gson();
//
//        ItemStack[] contents = inventory.getContents();
//
//        for (int i = 0; i < inventory.getSize(); i++) {
//            ItemStack item = inventory.getItem(i);
//
//            String serializedJSON = gson.toJson(item.serialize());
//
//        }
    }

    override fun onPluginStart() {
        loadData()
    }

    override fun onPluginEnd() {
        saveData()
    }
}