package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.thebluehats.server.game.managers.enchants.HitCounter;

public class HitCounterModule extends AbstractModule {
    @Provides
    static HitCounter provideHitCounter(Injector injector) {
        return injector.getInstance(HitCounter.class);
    }

    @Override
    protected void configure() { }
}
