package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerifier;
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerifier;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;

public class DamageEnchantTriggersModule extends AbstractModule {
    @Provides
    @Singleton
    static PlayerDamageTrigger providePlayerDamageTrigger(CustomEnchantUtils customEnchantUtils, PlayerHitPlayerVerifier playerHitPlayerVerifier) {
        return new PlayerDamageTrigger(customEnchantUtils, playerHitPlayerVerifier);
    }

    @Provides
    @Singleton
    static ArrowDamageTrigger provideArrowDamageTrigger(CustomEnchantUtils customEnchantUtils, ArrowHitPlayerVerifier arrowHitPlayerVerifier) {
        return new ArrowDamageTrigger(customEnchantUtils, arrowHitPlayerVerifier);
    }

    @Override
    protected void configure() { }
}
