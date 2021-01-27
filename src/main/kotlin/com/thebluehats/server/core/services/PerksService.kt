package com.thebluehats.server.core.services

import com.google.inject.Injector
import com.thebluehats.server.game.managers.world.PerkManager
import com.thebluehats.server.game.perks.Perk
import com.thebluehats.server.game.perks.Vampire
import com.thebluehats.server.game.utils.Registerer

class PerksService : Service {
    override fun provision(injector: Injector) {
        val registerer: Registerer<Perk> = injector.getInstance(PerkManager::class.java)
        registerer.register(injector.getInstance(Vampire::class.java))
    }
}