package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;

public class EventTemplatesModule extends AbstractModule {
    @Provides
    @Singleton
    static PlayerHitPlayerTemplate providePlayerHitPlayerTemplate(CustomEnchantUtils customEnchantUtils) {
        return new PlayerHitPlayerTemplate(customEnchantUtils);
    }

    @Provides
    @Singleton
    static ArrowHitPlayerTemplate provideArrowHitPlayerTemplate(CustomEnchantUtils customEnchantUtils) {
        return new ArrowHitPlayerTemplate(customEnchantUtils);
    }

    @Override
    protected void configure() {
    }
}
