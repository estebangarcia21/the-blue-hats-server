package com.thebluehats.server.game.world

import com.thebluehats.server.game.managers.enchants.GlobalPlayerTimerListener
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.plugin.java.JavaPlugin
import javax.inject.Inject

class PlayableArea @Inject constructor(private val regionManager: RegionManager, private val plugin: JavaPlugin) :
    GlobalPlayerTimerListener {
    override fun onTimeStep(player: Player) {
        if (regionManager.entityIsInPlayableArea(player)) return

        plugin.server.pluginManager.callEvent(PlayerDeathEvent(player, null, 0, ""))
        player.sendMessage(ChatColor.RED.toString() + "Congratulations! You went out of the map!")

    }

    override val tickDelay: Long = 1L
}