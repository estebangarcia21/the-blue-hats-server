package com.thebluehats.server.game.managers.world

import com.thebluehats.server.game.perks.Perk
import com.thebluehats.server.game.utils.Registerer
import java.util.*

class PerkManager : Registerer<Perk> {
    private val perks = ArrayList<Perk>()

    override fun register(vararg objects: Perk) {
        this.perks.addAll(listOf(*objects))
    }
}