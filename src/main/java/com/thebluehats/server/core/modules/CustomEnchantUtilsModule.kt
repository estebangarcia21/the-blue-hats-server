package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils
import com.thebluehats.server.game.utils.RomanNumeralConverter

class CustomEnchantUtilsModule : AbstractModule() {
    override fun configure() {}

    companion object {
        @Provides
        @Singleton
        fun provideCustomEnchantUtils(romanNumeralConverter: RomanNumeralConverter): CustomEnchantUtils {
            return CustomEnchantUtils(romanNumeralConverter)
        }
    }
}