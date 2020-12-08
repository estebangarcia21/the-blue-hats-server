package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.managers.combat.DamageManager;

public class DamageManagerModule extends AbstractModule {
    @Provides
    @Singleton
    static DamageManager provideDamageManager(Injector injector) {
        return injector.getInstance(DamageManager.class);
    }

    @Override
    protected void configure() { }
}
