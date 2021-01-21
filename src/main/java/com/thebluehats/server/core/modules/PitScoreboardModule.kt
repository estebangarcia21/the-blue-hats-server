package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.thebluehats.server.api.daos.PerformanceStatsService
import com.thebluehats.server.game.managers.combat.CombatManager
import com.thebluehats.server.game.managers.world.PitScoreboard

class PitScoreboardModule : AbstractModule() {
    override fun configure() {}

    companion object {
        @Provides
        @Singleton
        fun providePitScoreboard(
            combatManager: CombatManager?,
            performanceStatsService: PerformanceStatsService?
        ): PitScoreboard {
            return PitScoreboard(combatManager, performanceStatsService)
        }
    }
}