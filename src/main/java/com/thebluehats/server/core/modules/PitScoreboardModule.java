package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.api.daos.PerformanceStatsService;
import com.thebluehats.server.game.managers.combat.CombatManager;
import com.thebluehats.server.game.managers.world.PitScoreboard;

public class PitScoreboardModule extends AbstractModule {
    @Provides
    @Singleton
    static PitScoreboard providePitScoreboard(CombatManager combatManager, PerformanceStatsService performanceStatsService) {
        return new PitScoreboard(combatManager, performanceStatsService);
    }

    @Override
    protected void configure() { }
}
