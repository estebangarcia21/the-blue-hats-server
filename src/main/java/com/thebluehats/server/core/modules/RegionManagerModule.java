package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager;

public class RegionManagerModule extends AbstractModule {
    @Provides
    @Singleton
    static RegionManager provideRegionManager() {
        return new RegionManager();
    }

    @Override
    protected void configure() {
    }
}
