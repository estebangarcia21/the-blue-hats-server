package com.thebluehats.server.game.utils

import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

interface DataInitializer : Listener {
    fun initializeDataOnPlayerJoin(event: PlayerJoinEvent)
}