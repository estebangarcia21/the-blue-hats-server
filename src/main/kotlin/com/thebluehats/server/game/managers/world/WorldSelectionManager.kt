package com.thebluehats.server.game.managers.world

import com.google.inject.Inject
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager
import com.thebluehats.server.game.utils.LoreParser
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class WorldSelectionManager @Inject constructor(
    private val plugin: JavaPlugin,
    private val regionManager: RegionManager
) : Listener {
    private val inventoryName = ChatColor.LIGHT_PURPLE.toString() + "World Selection"
    private val gui = Bukkit.createInventory(null, 9, inventoryName)
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        player.teleport(regionManager.getSpawnLocation(player))
    }

    fun displaySelectionMenu(player: Player) {
        Bukkit.getServer().scheduler.scheduleSyncDelayedTask(plugin, {
            generateGui()
            player.teleport(Location(player.world, -90.5, 60.0, 0.5))
            player.openInventory(gui)
        }, 1L)
    }

    private fun generateGui() {
        gui.setItem(3, ItemStack(Material.BLAZE_POWDER))
        var meta = gui.getItem(3).itemMeta
        meta.displayName = ChatColor.RED.toString() + "The Toxic World"
        val toxicWorldLore = LoreParser(
            "A world where any<br/>enchants are allowed<br/><br/><italic>No token limit on items</italic>"
        ).parse()
        meta.lore = toxicWorldLore
        gui.getItem(3).itemMeta = meta
        gui.setItem(5, ItemStack(Material.QUARTZ))
        meta = gui.getItem(5).itemMeta
        meta.displayName = ChatColor.AQUA.toString() + "The Peaceful World"
        val peacefulWorldLore = LoreParser(
            "A world where the most toxic<br/>enchants are removed from<br/>existance for peaceful<br/>gameplay and fair fights<br/><br/><italic>8 tokens maximum on items</italic>"
        )
            .parse()
        meta.lore = peacefulWorldLore
        gui.getItem(5).itemMeta = meta
    }
}