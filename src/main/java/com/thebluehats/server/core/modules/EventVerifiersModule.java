package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerifier;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerifier;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;

public class EventVerifiersModule extends AbstractModule {
    @Provides
    @Singleton
    static PlayerHitPlayerVerifier providePlayerHitPlayerVerifier(CustomEnchantUtils customEnchantUtils) {
        return new PlayerHitPlayerVerifier();
    }

    @Provides
    @Singleton
    static ArrowHitPlayerVerifier provideArrowHitPlayerVerifier(CustomEnchantUtils customEnchantUtils) {
        return new ArrowHitPlayerVerifier();
    }

    @Override
    protected void configure() {
    }
}
