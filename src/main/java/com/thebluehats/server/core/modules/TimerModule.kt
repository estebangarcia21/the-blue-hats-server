package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.thebluehats.server.game.managers.enchants.Timer
import org.bukkit.plugin.java.JavaPlugin

class TimerModule : AbstractModule() {
    override fun configure() {}

    companion object {
        @Provides
        fun provideTimer(plugin: JavaPlugin): Timer<*> {
            return Timer<Any>(plugin)
        }
    }
}