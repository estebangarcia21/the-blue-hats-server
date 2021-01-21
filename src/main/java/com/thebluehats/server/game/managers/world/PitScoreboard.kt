package com.thebluehats.server.game.managers.world

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
import com.thebluehats.server.game.managers.enchants.Timer.TimerData
import org.bukkit.Bukkit
import java.lang.Runnable
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
import com.thebluehats.server.game.managers.enchants.PostEventExecutor
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger
import com.thebluehats.server.game.utils.EntityValidator
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import org.bukkit.scoreboard.Team
import com.thebluehats.server.game.managers.world.Board
import org.bukkit.scoreboard.Objective
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
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*
import javax.inject.Inject
import kotlin.jvm.JvmOverloads

class PitScoreboard @Inject constructor(
    private val combatManager: CombatManager,
    private val performanceStatsService: PerformanceStatsService
) : Listener, GlobalTimerListener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        buildScoreboard(event.player)
    }

    fun buildScoreboard(player: Player) {
        val scoreboard = Bukkit.getScoreboardManager().newScoreboard
        val optimizedBoard = Board(scoreboard)
        val simpleDateFormat = SimpleDateFormat("MM/dd/yy")
        val date = Date()
        val decimalFormat = DecimalFormat("#0.00")
        optimizedBoard.title(ChatColor.YELLOW.toString() + ChatColor.BOLD + "THE BLUE HATS PIT")
        optimizedBoard.line(appendColors(ChatColor.GRAY.toString() + simpleDateFormat.format(date) + " " + ChatColor.DARK_GRAY + "mega69L"))
        optimizedBoard.line(" ")
        optimizedBoard.line(formatLine("Prestige", ChatColor.YELLOW, "XXXV", false))
        optimizedBoard.line(formatLine("Level", ChatColor.AQUA, "[120]", true))
        optimizedBoard.line(formatLine("XP", ChatColor.AQUA, "MAXED!", false))
        optimizedBoard.line("  ")
        optimizedBoard.line(
            formatLine(
                "Gold",
                ChatColor.GOLD,
                decimalFormat.format(performanceStatsService.getPlayerGold(player)),
                false
            )
        )
        optimizedBoard.line("   ")
        optimizedBoard.line(formatLine("Status", null, formatStatus(player), false))
        optimizedBoard.line("    ")
        optimizedBoard.line(appendColors(ChatColor.YELLOW.toString() + "play.thebluehatspit.net"))
        player.scoreboard = scoreboard
    }

    private fun formatLine(key: String, color: ChatColor?, value: String, isBold: Boolean): String {
        val finalColor = color?.toString() ?: ""
        val boldColor = if (isBold) ChatColor.BOLD.toString() else ""
        val valueColor = finalColor + boldColor
        return appendColors(ChatColor.WHITE.toString() + key + ": " + valueColor + value)
    }

    private fun appendColors(string: String): String {
        if (string.length >= 16) {
            val base = string.substring(0, 16)
            val end = string.substring(16)
            val colors = ArrayList<String>()
            for (i in base.length - 1 downTo 0) {
                if (base[i] == ChatColor.COLOR_CHAR) {
                    colors.add(base.substring(i, i + 2))
                    val charCheck = i - 2
                    if (charCheck < 0) continue
                    if (base[charCheck] != ChatColor.COLOR_CHAR) break
                }
            }
            val modifiers = StringBuilder()
            val colorsSize = colors.size
            for (i in 0 until colorsSize - 1) {
                modifiers.append(colors[i])
            }
            return base + colors[colorsSize - 1] + modifiers + end
        }
        return string
    }

    private fun formatStatus(player: Player): String {
        val combatStatus = combatManager.getStatus(player)
        val formattedStatus = combatStatus.formattedStatus
        return if (combatStatus === CombatStatus.COMBAT) formattedStatus + " " + ChatColor.RESET + ChatColor.GRAY + "(" + combatManager.getCombatTime(
            player
        ) + ")" else formattedStatus
    }

    override fun onTick(player: Player) {
        buildScoreboard(player)
    }

    override val tickDelay: Long
        get() = 20L
}