package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.thebluehats.server.game.managers.world.WorldSelectionManager
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager
import org.bukkit.plugin.java.JavaPlugin

class WorldSelectionManagerModule : AbstractModule() {
    override fun configure() {}

    companion object {
        @Provides
        @Singleton
        fun provideWorldSelectionManager(plugin: JavaPlugin?, regionManager: RegionManager?): WorldSelectionManager {
            return WorldSelectionManager(plugin, regionManager)
        }
    }
}