package com.thebluehats.server.game.events

import com.google.inject.Inject
import com.thebluehats.server.game.managers.enchants.GlobalTimerListener
import com.thebluehats.server.game.utils.Registerer
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

class GameEventManager @Inject constructor(private val plugin: JavaPlugin, override val tickDelay: Long = 1200L) :
    Registerer<GameEvent>,
GlobalTimerListener {
    private val events = arrayListOf<GameEvent>()

    override fun register(vararg objects: GameEvent) {
        this.events.addAll(objects)
    }

    override fun onTimeStep() {
        val event = events[Random.nextInt(events.size)]

        event.onStart()

        Bukkit.getServer().scheduler.scheduleSyncDelayedTask(plugin, event::onEnd, 36000L)
    }
}
