package com.thebluehats.server.game.managers.game;

import java.util.ArrayList;
import java.util.UUID;

import com.google.inject.Inject;
import com.thebluehats.server.game.utils.LoreBuilder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldSelectionManager implements Listener {
    private final String inventoryName = ChatColor.LIGHT_PURPLE + "World Selection";
    private final Inventory gui = Bukkit.createInventory(null, 9, inventoryName);

    private final ArrayList<UUID> mayExitGuiSelection = new ArrayList<>();
    private JavaPlugin plugin;

    @Inject
    public WorldSelectionManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void displaySelectionMenu(Player player) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            generateGui();

            player.teleport(new Location(player.getWorld(), -90.5, 60, 0.5));
            player.openInventory(gui);
        }, 1L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        displaySelectionMenu(event.getPlayer());
    }

    // @EventHandler
    // public void onChunkUnload(ChunkUnloadEvent event) {
    // event.setCancelled(true);
    // }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(inventoryName)) {
            if (event.getRawSlot() == 3) {
                mayExitGuiSelection.add(event.getWhoClicked().getUniqueId());

                transportToWorld(event.getWhoClicked(), "world");

                event.getWhoClicked().closeInventory();
            }

            if (event.getRawSlot() == 5) {
                mayExitGuiSelection.add(event.getWhoClicked().getUniqueId());

                transportToWorld(event.getWhoClicked(), "ThePit_0");

                event.getWhoClicked().closeInventory();
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(inventoryName)) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (!mayExitGuiSelection.contains(event.getPlayer().getUniqueId())) {
                    event.getPlayer().openInventory(gui);
                } else {
                    mayExitGuiSelection.remove(event.getPlayer().getUniqueId());
                }
            }, 1L);
        }
    }

    private void transportToWorld(HumanEntity player, String worldName) {
        World world = plugin.getServer().createWorld(new WorldCreator(worldName));
        Location location = RegionManager.getInstance().getSpawnLocation(((Player) player));
        Location spawnLocation = new Location(world, location.getX(), location.getY(), location.getZ(),
                location.getYaw(), location.getPitch());

        Chunk centerChunk = location.getChunk();
        centerChunk.load(true);

        player.sendMessage(ChatColor.GREEN + "You will be teleported soon...");

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.teleport(spawnLocation), 20L);
    }

    private void generateGui() {
        gui.setItem(3, new ItemStack(Material.BLAZE_POWDER));

        ItemMeta meta = gui.getItem(3).getItemMeta();

        meta.setDisplayName(ChatColor.RED + "The Toxic World");

        meta.setLore(new LoreBuilder().setColor(ChatColor.YELLOW).write("A world where any").next()
                .write("enchants are allowed").next().next().resetColor()
                .write(ChatColor.ITALIC + "No token limit on items").build());

        gui.getItem(3).setItemMeta(meta);

        gui.setItem(5, new ItemStack(Material.QUARTZ));

        meta = gui.getItem(5).getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "The Peaceful World");

        meta.setLore(new LoreBuilder().setColor(ChatColor.WHITE).write("A world where the most toxic").next()
                .write("enchants are removed from").next().write("existance for peaceful").next()
                .write("gameplay and fair fights").next().next().resetColor()
                .write(ChatColor.ITALIC + "8 tokens maximum on items").build());

        gui.getItem(5).setItemMeta(meta);
    }
}