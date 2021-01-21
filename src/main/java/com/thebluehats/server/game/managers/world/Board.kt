/*
 * This class is taken from Exerosis's Board.java gist.
 * https://gist.github.com/Exerosis/f422c17dde154cca65cde4c4e4b43ca3
 *
 * This is NOT THE WORK of The Blue Hats development team. All credit goes to https://gist.github.com/Exerosis.
 *
 * This file was slightly modified by The Blue Hats development team.
 */
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
import org.bukkit.scoreboard.Scoreboard
import kotlin.jvm.JvmOverloads

/**
 * A 32 character no flicker scoreboard implementation, fast and lightweight.
 */
class Board @JvmOverloads constructor(board: Scoreboard = Bukkit.getScoreboardManager().mainScoreboard) {
    private val teams = arrayOfNulls<Team>(MAX_LINES)
    private val objective: Objective

    companion object {
        private const val MAX_LINES = 15
        private const val MAX_CHARACTERS = 0
        private val BLANKS = arrayOfNulls<String>(MAX_LINES)

        init {
            MAX_CHARACTERS = if (!Bukkit.getServer().version.contains("1.1")) 16 else 64
            for (i in 0 until MAX_LINES) BLANKS[i] =
                String(charArrayOf(ChatColor.COLOR_CHAR, ('s'.toInt() + i).toChar()))
        }
    }

    /**
     * Sets the title to the given value.
     *
     * @param title
     * - The new title.
     */
    fun title(title: Any) {
        objective.displayName = title.toString()
    }
    /**
     * Sets the line at a given index to the given text and score.
     *
     * @param index
     * - The index of the line.
     * @param text
     * - The text 32-128 characters max.
     * @param score
     * - The score to display for this line.
     * @return - The index of the line.
     */
    /**
     * Sets the line at a given index to the given text with a score of 1.
     *
     * @param index
     * - The index of the line.
     * @param text
     * - The text. ~32 characters max.
     * @return - The index of the line.
     */
    @JvmOverloads
    fun line(index: Int, text: String, score: Int = MAX_LINES - index): Int {
        val max = MAX_CHARACTERS
        val min = MAX_CHARACTERS - 1
        val split = if (text.length < max) 0 else if (text[min] == '§') min else max
        teams[index]!!.prefix = if (split == 0) text else text.substring(0, split)
        teams[index]!!.suffix = if (split == 0) "" else text.substring(split)
        objective.getScore(BLANKS[index]).score = score
        return index
    }

    /**
     * Sets the first empty line to the given text with a score of 1.
     *
     * @param text
     * - The text. ~32 characters max.
     * @return - The index of the line.
     */
    fun line(text: String): Int {
        for (i in 0 until MAX_LINES) if (teams[i]!!.prefix.isEmpty) return line(i, text)
        throw NoSuchElementException("No empty lines")
    }

    /**
     * Removes the line at the given index.
     *
     * @param index
     * - The index of the line.
     * @return - `true` if the line was previously set.
     */
    fun remove(index: Int): Boolean {
        if (index >= MAX_LINES) return false
        objective.scoreboard.resetScores(BLANKS[index])
        return true
    }
    /**
     * Construct a new [Scoreboard] wrapping the given [org.bukkit.scoreboard.Scoreboard].
     *
     * @param board
     * - The [org.bukkit.scoreboard.Scoreboard] to wrap.
     */
    /**
     * Construct a new [Scoreboard] wrapping the main [org.bukkit.scoreboard.Scoreboard].
     */
    init {
        board.clearSlot(DisplaySlot.SIDEBAR)
        objective = board.registerNewObjective("sidebar", "dummy")
        objective.displaySlot = DisplaySlot.SIDEBAR
        for (i in 0 until MAX_LINES) board.registerNewTeam(BLANKS[i]).also { teams[i] = it }
            .addEntry(BLANKS[i])
    }
}