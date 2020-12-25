package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.core.modules.annotations.MirrorReference;
import com.thebluehats.server.game.enchants.Mirror;
import com.thebluehats.server.game.managers.combat.CombatManager;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager;

public class DamageManagerModule extends AbstractModule {
    @Provides
    @Singleton
    static DamageManager provideDamageManager(@MirrorReference Mirror mirror, CombatManager combatManager,
            CustomEnchantUtils customEnchantUtils, RegionManager regionManager) {
        return new DamageManager(mirror, combatManager, customEnchantUtils, regionManager);
    }

    @Override
    protected void configure() {
    }
}
