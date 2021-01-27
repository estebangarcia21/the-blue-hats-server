package com.thebluehats.server.game.managers.enchants

import com.thebluehats.server.game.managers.enchants.processedevents.CastedEvent
import org.bukkit.event.Event
import org.bukkit.event.Listener

interface PostEventExecutor<E : Event?, T : CastedEvent<*>> : Listener {
    fun onEvent(event: E)
    fun execute(data: T)
}