package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.managers.combat.CombatManager;
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager;

import org.bukkit.plugin.java.JavaPlugin;

public class CombatManagerModule extends AbstractModule {
    @Provides
    @Singleton
    static CombatManager provideCombatManager(JavaPlugin plugin, RegionManager regionManager) {
        return new CombatManager(plugin, regionManager);
    }

    @Override
    protected void configure() {
    }
}
