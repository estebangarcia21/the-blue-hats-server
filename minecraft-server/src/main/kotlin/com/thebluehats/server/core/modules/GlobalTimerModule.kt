package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.thebluehats.server.game.managers.enchants.GlobalTimer
import org.bukkit.plugin.java.JavaPlugin

class GlobalTimerModule : AbstractModule() {
    override fun configure() {}

    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun provideGlobalTimer(plugin: JavaPlugin): GlobalTimer {
            return GlobalTimer(plugin)
        }
    }
}