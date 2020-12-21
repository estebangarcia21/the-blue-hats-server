package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.managers.game.WorldSelectionManager;
import com.thebluehats.server.game.managers.game.regionmanager.RegionManager;

import org.bukkit.plugin.java.JavaPlugin;

public class WorldSelectionManagerModule extends AbstractModule {
    @Provides
    @Singleton
    static WorldSelectionManager provideWorldSelectionManager(JavaPlugin plugin, RegionManager regionManager) {
        return new WorldSelectionManager(plugin, regionManager);
    }

    @Override
    protected void configure() {
    }
}
