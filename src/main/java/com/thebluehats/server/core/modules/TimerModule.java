package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.thebluehats.server.game.managers.enchants.Timer;

import org.bukkit.plugin.java.JavaPlugin;

public class TimerModule extends AbstractModule {
    @Provides
    static Timer<?> provideTimer(JavaPlugin plugin) {
        return new Timer<>(plugin);
    }

    @Override
    protected void configure() {
    }
}
