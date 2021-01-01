package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.managers.enchants.GlobalTimer;
import org.bukkit.plugin.java.JavaPlugin;

public class GlobalTimerModule extends AbstractModule {
    @Provides
    @Singleton
    static GlobalTimer provideGlobalTimer(JavaPlugin plugin) {
        return new GlobalTimer(plugin);
    }

    @Override
    protected void configure() {
    }
}
