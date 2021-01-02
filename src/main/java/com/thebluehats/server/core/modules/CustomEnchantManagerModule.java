package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.utils.PantsDataContainer;
import com.thebluehats.server.game.utils.RomanNumeralConverter;

import org.bukkit.plugin.java.JavaPlugin;

public class CustomEnchantManagerModule extends AbstractModule {
    @Provides
    @Singleton
    static CustomEnchantManager provideCustomEnchantManager(JavaPlugin plugin,
            RomanNumeralConverter romanNumeralConverter, PantsDataContainer pantsDataContainer,
            CustomEnchantUtils customEnchantUtils) {
        return new CustomEnchantManager(plugin, romanNumeralConverter, pantsDataContainer, customEnchantUtils);
    }

    @Override
    protected void configure() {
    }
}
