package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerifier
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerifier
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils

class EventVerifiersModule : AbstractModule() {
    override fun configure() {}

    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun providePlayerHitPlayerVerifier(customEnchantUtils: CustomEnchantUtils): PlayerHitPlayerVerifier {
            return PlayerHitPlayerVerifier()
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideArrowHitPlayerVerifier(customEnchantUtils: CustomEnchantUtils): ArrowHitPlayerVerifier {
            return ArrowHitPlayerVerifier()
        }
    }
}