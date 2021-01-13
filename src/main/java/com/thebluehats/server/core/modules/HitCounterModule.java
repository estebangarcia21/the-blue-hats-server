package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.thebluehats.server.game.managers.enchants.HitCounter;

import com.thebluehats.server.game.managers.enchants.Timer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class HitCounterModule extends AbstractModule {
    @Provides
    static HitCounter provideHitCounter(Timer<UUID> timer) {
        return new HitCounter(timer);
    }

    @Override
    protected void configure() {
    }
}
