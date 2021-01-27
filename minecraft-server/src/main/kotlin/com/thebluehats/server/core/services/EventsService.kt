package com.thebluehats.server.core.services

import com.google.inject.Inject
import com.google.inject.Injector
import com.thebluehats.server.game.events.GameEvent
import com.thebluehats.server.game.events.GameEventManager
import com.thebluehats.server.game.events.ZombieApocalypse
import com.thebluehats.server.game.managers.enchants.GlobalTimer
import com.thebluehats.server.game.utils.Registerer

class EventsService @Inject constructor(private val gameEventRegisterer: Registerer<GameEvent>, private val
globalTimer: GlobalTimer) : Service {
    override fun provision(injector: Injector) {
        gameEventRegisterer.register(injector.getInstance(ZombieApocalypse::class.java))
    }
}