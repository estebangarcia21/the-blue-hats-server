package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.managers.combat.CombatManager;

import org.bukkit.plugin.java.JavaPlugin;

public class CombatManagerModule extends AbstractModule {
    @Provides
    @Singleton
    static CombatManager provideCombatManager(JavaPlugin plugin) {
        return new CombatManager(plugin);
    }

    @Override
    protected void configure() {
    }
}
