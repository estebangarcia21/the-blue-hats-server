package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.thebluehats.server.game.events.GameEventManager
import org.bukkit.plugin.java.JavaPlugin

class GameEventManagerModule : AbstractModule() {
    override fun configure() {}

    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun provideGameEventManager(plugin: JavaPlugin): GameEventManager {
            return GameEventManager(plugin)
        }
    }
}