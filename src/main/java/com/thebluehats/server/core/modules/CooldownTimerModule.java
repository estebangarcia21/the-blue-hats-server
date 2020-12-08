package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.thebluehats.server.game.managers.enchants.CooldownTimer;

import org.bukkit.plugin.java.JavaPlugin;

public class CooldownTimerModule extends AbstractModule {
    @Provides
    static CooldownTimer provideCooldownTimer(JavaPlugin plugin) {
        return new CooldownTimer(plugin);
    }

    @Override
    protected void configure() {
    }
}
