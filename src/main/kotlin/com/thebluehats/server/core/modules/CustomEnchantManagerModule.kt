package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils
import com.thebluehats.server.game.utils.PantsData
import org.bukkit.plugin.java.JavaPlugin

class CustomEnchantManagerModule : AbstractModule() {
    override fun configure() {}

    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun provideCustomEnchantManager(
            plugin: JavaPlugin,
            pantsData: PantsData,
            customEnchantUtils: CustomEnchantUtils
        ): CustomEnchantManager {
            return CustomEnchantManager(plugin, pantsData, customEnchantUtils)
        }
    }
}