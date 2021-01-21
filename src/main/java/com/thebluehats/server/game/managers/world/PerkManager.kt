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
import java.util.ArrayList
import kotlin.jvm.JvmOverloads

class PerkManager : Registerer<Perk?> {
    private val perks = ArrayList<Perk>()
    override fun register(perks: Array<Perk>) {
        this.perks.addAll(Arrays.asList(*perks))
    }
}