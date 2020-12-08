package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.managers.combat.CombatManager;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;

public class DamageManagerModule extends AbstractModule {
    @Provides
    @Singleton
    static DamageManager provideDamageManager(CustomEnchantManager customEnchantManager, CombatManager combatManager) {
        return new DamageManager(customEnchantManager, combatManager);
    }

    @Override
    protected void configure() {
    }
}
