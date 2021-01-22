package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.thebluehats.server.core.TheBlueHatsServerPlugin
import com.thebluehats.server.game.managers.enchants.CustomEnchant
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager
import com.thebluehats.server.game.utils.PluginLifecycleListener
import com.thebluehats.server.game.utils.Registerer

class RegistererModule : AbstractModule() {
    override fun configure() {}

    companion object {
        @Provides
        @Singleton
        fun provideCustomEnchantManager(customEnchantManager: CustomEnchantManager): Registerer<CustomEnchant> {
            return customEnchantManager
        }

        @Provides
        @Singleton
        fun providePluginLifecycleListenerRegisterer(theBlueHatsServerPlugin: TheBlueHatsServerPlugin): Registerer<PluginLifecycleListener> {
            return theBlueHatsServerPlugin
        }
    }
}