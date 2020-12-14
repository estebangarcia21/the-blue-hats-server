package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;

public class CustomEnchantUtilsModule extends AbstractModule {
    @Provides
    @Singleton
    static CustomEnchantUtils provideCustomEnchantUtils() {
        return new CustomEnchantUtils();
    }

    @Override
    protected void configure() {
    }
}
