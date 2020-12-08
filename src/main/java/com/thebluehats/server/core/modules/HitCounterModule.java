package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.thebluehats.server.game.managers.enchants.HitCounter;

import org.bukkit.plugin.java.JavaPlugin;

public class HitCounterModule extends AbstractModule {
    @Provides
    static HitCounter provideHitCounter(JavaPlugin plugin) {
        return new HitCounter(plugin);
    }

    @Override
    protected void configure() {
    }
}
