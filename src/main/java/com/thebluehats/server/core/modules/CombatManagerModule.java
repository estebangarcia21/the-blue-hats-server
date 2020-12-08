package com.thebluehats.server.core.modules;

import com.google.inject.*;
import com.thebluehats.server.game.managers.combat.CombatManager;

public class CombatManagerModule extends AbstractModule {
    @Provides
    @Singleton
    static CombatManager provideCombatManager(Injector injector) {
        return injector.getInstance(CombatManager.class);
    }

    @Override
    public void configure() { }
}
