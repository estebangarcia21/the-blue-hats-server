package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.thebluehats.server.game.managers.enchants.CooldownTimer;

public class CooldownTimerModule extends AbstractModule {
    @Provides
    static CooldownTimer provideCooldownTimer(Injector injector) {
        return injector.getInstance(CooldownTimer.class);
    }

    @Override
    public void configure() { }
}
