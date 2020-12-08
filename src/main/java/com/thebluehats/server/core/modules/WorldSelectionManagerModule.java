package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.thebluehats.server.game.managers.game.WorldSelectionManager;

public class WorldSelectionManagerModule extends AbstractModule {
    @Provides
    static WorldSelectionManager provideWorldSelectionManager(Injector injector) {
        return injector.getInstance(WorldSelectionManager.class);
    }

    @Override
    public void configure() { }
}
