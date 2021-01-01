package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerificationTemplate;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerificationTemplate;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;

public class EventTemplatesModule extends AbstractModule {
    @Provides
    @Singleton
    static PlayerHitPlayerVerificationTemplate providePlayerHitPlayerTemplate(CustomEnchantUtils customEnchantUtils) {
        return new PlayerHitPlayerVerificationTemplate(customEnchantUtils);
    }

    @Provides
    @Singleton
    static ArrowHitPlayerVerificationTemplate provideArrowHitPlayerTemplate(CustomEnchantUtils customEnchantUtils) {
        return new ArrowHitPlayerVerificationTemplate(customEnchantUtils);
    }

    @Override
    protected void configure() {
    }
}
