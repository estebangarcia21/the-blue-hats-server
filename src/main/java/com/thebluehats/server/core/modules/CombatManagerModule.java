package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.managers.combat.CombatManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerifier;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerifier;
import com.thebluehats.server.game.managers.enchants.Timer;
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager;

import java.util.UUID;

public class CombatManagerModule extends AbstractModule {
    @Provides
    @Singleton
    static CombatManager provideCombatManager(RegionManager regionManager, Timer<UUID> timer, PlayerHitPlayerVerifier playerHitPlayerVerifier, ArrowHitPlayerVerifier arrowHitPlayerVerifier) {
        return new CombatManager(regionManager, timer, playerHitPlayerVerifier, arrowHitPlayerVerifier);
    }

    @Override
    protected void configure() {
    }
}
