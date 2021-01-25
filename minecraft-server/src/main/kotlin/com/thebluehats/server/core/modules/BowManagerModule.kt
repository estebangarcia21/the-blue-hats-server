package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.thebluehats.server.game.managers.combat.BowManager

class BowManagerModule : AbstractModule() {
    override fun configure() {}

    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun provideBowManager(): BowManager {
            return BowManager()
        }
    }
}