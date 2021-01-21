package com.thebluehats.server.game.managers.world

import com.google.inject.Inject
import com.thebluehats.server.game.utils.RomanNumeralConverter.convertRomanNumeralToInteger
import com.thebluehats.server.game.utils.PantsData.data
import com.thebluehats.server.game.utils.PantsData.PantsDataValue.textColor
import com.thebluehats.server.game.utils.RomanNumeralConverter.convertToRomanNumeral
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger.run
import com.thebluehats.server.api.daos.PerformanceStatsService.getPlayerGold
import com.thebluehats.server.game.managers.combat.CombatManager.getStatus
import com.thebluehats.server.game.managers.combat.CombatStatus.formattedStatus
import com.thebluehats.server.game.managers.combat.CombatManager.getCombatTime
import com.thebluehats.server.game.utils.LoreParser.parse
import org.bukkit.plugin.java.JavaPlugin
import java.util.HashMap
import com.thebluehats.server.game.managers.enchants.Timer.TimerData
import org.bukkit.Bukkit
import java.lang.Runnable
import java.util.UUID
import com.thebluehats.server.game.managers.enchants.HitCounter.HitCounterData
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import com.thebluehats.server.game.managers.enchants.processedevents.CastedEntityDamageByEntityEvent
import com.thebluehats.server.game.managers.enchants.processedevents.CastedEvent
import com.thebluehats.server.game.utils.PluginLifecycleListener
import com.thebluehats.server.game.managers.enchants.GlobalTimerListener
import com.thebluehats.server.game.managers.enchants.EnchantGroup
import java.lang.SafeVarargs
import java.lang.IllegalArgumentException
import com.thebluehats.server.game.utils.RomanNumeralConverter
import com.thebluehats.server.game.managers.enchants.CustomEnchant
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils.ItemEnchantData
import com.thebluehats.server.game.utils.PantsData
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils
import com.thebluehats.server.game.utils.Registerer
import com.thebluehats.server.game.utils.SortCustomEnchantByName
import org.bukkit.inventory.meta.ItemMeta
import com.thebluehats.server.game.utils.PantsData.FreshPantsColor
import com.thebluehats.server.game.utils.PantsData.PantsDataValue
import java.util.Arrays
import com.thebluehats.server.game.managers.enchants.PostEventExecutor
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger
import com.thebluehats.server.game.utils.EntityValidator
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import org.bukkit.scoreboard.Team
import com.thebluehats.server.game.managers.world.Board
import org.bukkit.scoreboard.Objective
import java.util.NoSuchElementException
import org.bukkit.scoreboard.DisplaySlot
import com.thebluehats.server.game.managers.world.regionmanager.maps.response.Spawn
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.entity.Arrow
import com.thebluehats.server.game.perks.Perk
import com.thebluehats.server.game.managers.combat.CombatManager
import com.thebluehats.server.api.daos.PerformanceStatsService
import org.bukkit.event.player.PlayerJoinEvent
import java.text.SimpleDateFormat
import java.text.DecimalFormat
import java.lang.StringBuilder
import com.thebluehats.server.game.managers.combat.CombatStatus
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager
import org.bukkit.inventory.Inventory
import com.thebluehats.server.game.utils.LoreParser
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import kotlin.jvm.JvmOverloads

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
            player.teleport(Location(player.world, -90.5, 60, 0.5))
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