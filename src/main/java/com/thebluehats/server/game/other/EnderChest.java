package com.thebluehats.server.game.other;

import com.thebluehats.server.api.utils.DataLoader;
import com.thebluehats.server.game.utils.PluginLifecycleListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.UUID;

public class EnderChest implements Listener, PluginLifecycleListener, DataLoader {
    private final HashMap<UUID, Inventory> enderChests = new HashMap<>();

    @EventHandler
    public void onEnderChestOpen(InventoryOpenEvent event) {
        HumanEntity player = event.getPlayer();

        if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
            event.setCancelled(true);

            player.openInventory(enderChests.computeIfAbsent(player.getUniqueId(), k -> createInventory()));
        }
    }

    private Inventory createInventory() {
        return Bukkit.createInventory(null, 54, ChatColor.GRAY + "Ender Chest");
    }

    @Override
    public void loadData() {

    }

    @Override
    public void saveData() {
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

    @Override
    public void onPluginStart() {
        loadData();
    }

    @Override
    public void onPluginEnd() {
        saveData();
    }
}
