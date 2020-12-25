package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.utils.RomanNumeralConverter;

public class CustomEnchantUtilsModule extends AbstractModule {
    @Provides
    @Singleton
    static CustomEnchantUtils provideCustomEnchantUtils(RomanNumeralConverter romanNumeralConverter) {
        return new CustomEnchantUtils(romanNumeralConverter);
    }

    @Override
    protected void configure() {
    }
}
