package com.thebluehats.server.api.models

import java.util.*

class PitDataModel(val playerUuid: UUID, var prestige: Int, var level: Int, var xp: Int, gold: Float) {
    var gold: Double

    init {
        this.gold = gold.toDouble()
    }
}