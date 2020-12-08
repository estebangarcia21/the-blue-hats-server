package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.managers.combat.BowManager;

public class BowManagerModule extends AbstractModule {
    @Provides
    @Singleton
    static BowManager provideBowManager() {
        return new BowManager();
    }

    @Override
    protected void configure() {
    }
}
