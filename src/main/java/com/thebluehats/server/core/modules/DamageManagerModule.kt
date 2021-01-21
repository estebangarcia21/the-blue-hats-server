package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.thebluehats.server.core.modules.annotations.MirrorReference
import com.thebluehats.server.game.enchants.Mirror
import com.thebluehats.server.game.managers.combat.CombatManager
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager

class DamageManagerModule : AbstractModule() {
    override fun configure() {}

    companion object {
        @Provides
        @Singleton
        fun provideDamageManager(
            @MirrorReference mirror: Mirror, combatManager: CombatManager,
            customEnchantUtils: CustomEnchantUtils, regionManager: RegionManager
        ): DamageManager {
            return DamageManager(mirror, combatManager, customEnchantUtils, regionManager)
        }
    }
}