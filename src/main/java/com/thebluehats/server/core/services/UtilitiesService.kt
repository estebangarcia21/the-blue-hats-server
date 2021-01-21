package com.thebluehats.server.core.services

import com.google.inject.Inject
import com.google.inject.Injector
import com.thebluehats.server.game.managers.combat.BowManager
import com.thebluehats.server.game.managers.combat.CombatManager
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.enchants.GlobalTimer
import com.thebluehats.server.game.managers.world.PitScoreboard
import com.thebluehats.server.game.managers.world.WorldSelectionManager
import com.thebluehats.server.game.other.Bread
import com.thebluehats.server.game.other.DamageIndicator
import com.thebluehats.server.game.other.EnderChest
import com.thebluehats.server.game.other.Obsidian
import com.thebluehats.server.game.utils.PluginLifecycleListener
import com.thebluehats.server.game.utils.Registerer
import com.thebluehats.server.game.world.*
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class UtilitiesService @Inject constructor(
    private val plugin: JavaPlugin,
    private val globalTimer: GlobalTimer,
    private val pluginLifecycleListenerRegisterer: Registerer<PluginLifecycleListener>
) : Service {
    override fun provision(injector: Injector) {
        Bukkit.getServer().pluginManager.registerEvents(injector.getInstance(DamageManager::class.java), plugin)
        Bukkit.getServer().pluginManager.registerEvents(injector.getInstance(CombatManager::class.java), plugin)
        Bukkit.getServer().pluginManager.registerEvents(
            injector.getInstance(
                WorldSelectionManager::class.java
            ), plugin
        )
        val pitScoreboard = injector.getInstance(PitScoreboard::class.java)
        Bukkit.getServer().pluginManager.registerEvents(pitScoreboard, plugin)
        Bukkit.getServer().pluginManager.registerEvents(injector.getInstance(Bread::class.java), plugin)
        Bukkit.getServer().pluginManager.registerEvents(
            injector.getInstance(
                EnderChest::class.java
            ), plugin
        )
        Bukkit.getServer().pluginManager.registerEvents(injector.getInstance(AntiHunger::class.java), plugin)
        Bukkit.getServer().pluginManager.registerEvents(injector.getInstance(DamageIndicator::class.java), plugin)
        Bukkit.getServer().pluginManager.registerEvents(injector.getInstance(BowManager::class.java), plugin)
        val obsidian = injector.getInstance(Obsidian::class.java)
        Bukkit.getServer().pluginManager.registerEvents(obsidian, plugin)
        Bukkit.getServer().pluginManager.registerEvents(injector.getInstance(NoFallDamage::class.java), plugin)
        Bukkit.getServer().pluginManager.registerEvents(injector.getInstance(AutoRespawn::class.java), plugin)
        Bukkit.getServer().pluginManager.registerEvents(injector.getInstance(ClearArrows::class.java), plugin)
        Bukkit.getServer().pluginManager.registerEvents(
            injector.getInstance(
                PlayerJoinLeaveMessages::class.java
            ), plugin
        )
        Bukkit.getServer().pluginManager.registerEvents(injector.getInstance(WorldProtection::class.java), plugin)
        Bukkit.getServer().pluginManager.registerEvents(injector.getInstance(StopLiquidFlow::class.java), plugin)
        Bukkit.getServer().pluginManager.registerEvents(injector.getInstance(SpawnProtection::class.java), plugin)
        globalTimer.addListener(injector.getInstance(PlayableArea::class.java))
        globalTimer.addListener(pitScoreboard)
        pluginLifecycleListenerRegisterer.register(arrayOf(obsidian, globalTimer))
    }
}