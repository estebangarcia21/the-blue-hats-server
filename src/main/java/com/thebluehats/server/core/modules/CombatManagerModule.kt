package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.thebluehats.server.game.managers.combat.CombatManager
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerifier
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerifier
import com.thebluehats.server.game.managers.enchants.Timer
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager
import java.util.*

class CombatManagerModule : AbstractModule() {
    override fun configure() {}

    companion object {
        @Provides
        @Singleton
        fun provideCombatManager(
            regionManager: RegionManager,
            timer: Timer<UUID>,
            playerHitPlayerVerifier: PlayerHitPlayerVerifier,
            arrowHitPlayerVerifier: ArrowHitPlayerVerifier
        ): CombatManager {
            return CombatManager(regionManager, timer, playerHitPlayerVerifier, arrowHitPlayerVerifier)
        }
    }
}