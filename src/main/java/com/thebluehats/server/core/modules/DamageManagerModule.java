package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.core.modules.annotations.MirrorReference;
import com.thebluehats.server.game.enchants.Mirror;
import com.thebluehats.server.game.managers.combat.CombatManager;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;

public class DamageManagerModule extends AbstractModule {
    @Provides
    @Singleton
    static DamageManager provideDamageManager(@MirrorReference Mirror mirror, CustomEnchantManager customEnchantManager,
            CombatManager combatManager, CustomEnchantUtils customEnchantUtils) {
        return new DamageManager(mirror, customEnchantManager, combatManager, customEnchantUtils);
    }

    @Override
    protected void configure() {
    }
}
