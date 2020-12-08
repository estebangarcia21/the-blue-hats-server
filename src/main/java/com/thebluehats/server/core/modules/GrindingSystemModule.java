package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.managers.game.GrindingSystem;

public class GrindingSystemModule extends AbstractModule {
    @Provides
    @Singleton
    static GrindingSystem provideGrindingSystem() {
        return new GrindingSystem();
    }

    @Override
    protected void configure() {
    }
}
