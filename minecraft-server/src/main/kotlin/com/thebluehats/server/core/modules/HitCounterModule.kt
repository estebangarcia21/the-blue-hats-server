package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.thebluehats.server.game.managers.enchants.HitCounter
import com.thebluehats.server.game.managers.enchants.Timer
import java.util.*

class HitCounterModule : AbstractModule() {
    override fun configure() {}

    companion object {
        @Provides
        @JvmStatic
        fun provideHitCounter(timer: Timer<UUID>): HitCounter {
            return HitCounter(timer)
        }
    }
}