package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerifier
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerifier
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils

class DamageEnchantTriggersModule : AbstractModule() {
    override fun configure() {}

    companion object {
        @Provides
        @Singleton
        fun providePlayerDamageTrigger(
            customEnchantUtils: CustomEnchantUtils,
            playerHitPlayerVerifier: PlayerHitPlayerVerifier
        ): PlayerDamageTrigger {
            return PlayerDamageTrigger(customEnchantUtils, playerHitPlayerVerifier)
        }

        @Provides
        @Singleton
        fun provideArrowDamageTrigger(
            customEnchantUtils: CustomEnchantUtils,
            arrowHitPlayerVerifier: ArrowHitPlayerVerifier
        ): ArrowDamageTrigger {
            return ArrowDamageTrigger(customEnchantUtils, arrowHitPlayerVerifier)
        }
    }
}