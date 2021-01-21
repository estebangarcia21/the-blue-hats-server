package com.thebluehats.server.game.managers.enchants

import java.util.concurrent.ThreadLocalRandom

class RandomPercentage {
    fun percentChance(percent: Int): Boolean {
        return ThreadLocalRandom.current().nextInt(0, 100) <= percent
    }
}