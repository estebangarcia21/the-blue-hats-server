package com.thebluehats.server.game.managers.world

import com.thebluehats.server.api.daos.PerformanceStatsService
import com.thebluehats.server.game.managers.combat.CombatManager
import com.thebluehats.server.game.managers.combat.CombatStatus
import com.thebluehats.server.game.managers.enchants.GlobalTimerListener
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PitScoreboard @Inject constructor(
    private val combatManager: CombatManager,
    private val performanceStatsService: PerformanceStatsService
) : Listener, GlobalTimerListener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) = buildScoreboard(event.player)

    private fun buildScoreboard(player: Player) {
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

            run loop@ {
                (base.length - 1 downTo 0).forEach { i ->
                    if (base[i] == ChatColor.COLOR_CHAR) {
                        colors.add(base.substring(i, i + 2))

                        val charCheck = i - 2

                        if (charCheck < 0) return@forEach

                        if (base[charCheck] != ChatColor.COLOR_CHAR) return@loop
                    }
                }
            }

            val modifiers = StringBuilder()
            val colorsSize = colors.size

            (0 until colorsSize - 1).forEach { i ->
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

    override fun onTick(player: Player) = buildScoreboard(player)

    override val tickDelay: Long get() = 20L
}