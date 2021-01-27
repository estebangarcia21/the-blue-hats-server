package com.thebluehats.server.game.events

import org.bukkit.Bukkit

class ZombieApocalypse(override val duration: Long = 1200L) : GameEvent {
    override fun onStart() {
        Bukkit.getOnlinePlayers().forEach { p -> p.sendMessage("Zombie apocalypse") }
    }

    override fun onEnd() {

    }
}
