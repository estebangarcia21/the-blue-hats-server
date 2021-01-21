package com.thebluehats.server.game.world

import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerJoinLeaveMessages : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.joinMessage =
            (ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Welcome Back to The Blue Hats Server! " + ChatColor.AQUA
                    + event.player.name + ChatColor.GREEN + " just joined the server!")
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        event.quitMessage =
            (ChatColor.RED.toString() + ChatColor.BOLD.toString() + "RIP! " + ChatColor.AQUA
                    + event.player.name + ChatColor.RED + " just left the server!")
    }
}