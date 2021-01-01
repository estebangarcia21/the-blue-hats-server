package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.core.PluginLifecycleListenerRegisterer;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;
import com.thebluehats.server.game.utils.PluginLifecycleListener;
import com.thebluehats.server.game.utils.Registerer;

public class RegistererModule extends AbstractModule {
    @Provides
    @Singleton
    static Registerer<CustomEnchant> provideCustomEnchantManager(CustomEnchantManager customEnchantManager) {
        return customEnchantManager;
    }

    @Provides
    @Singleton
    static Registerer<PluginLifecycleListener> providePluginLifecycleListenerRegisterer() {
        return new PluginLifecycleListenerRegisterer();
    }

    @Override
    protected void configure() { }
}
