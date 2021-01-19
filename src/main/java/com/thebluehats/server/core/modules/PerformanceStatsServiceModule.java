package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.api.daos.PerformanceStatsService;
import com.thebluehats.server.api.implementations.pitdata.PerformanceStatsServiceImpl;
import com.thebluehats.server.core.modules.annotations.ServerAPI;
import kong.unirest.UnirestInstance;

public class PerformanceStatsServiceModule extends AbstractModule {
    @Provides
    @Singleton
    static PerformanceStatsService provideGrindingSystem(@ServerAPI UnirestInstance serverAPI) {
        return new PerformanceStatsServiceImpl(serverAPI);
    }

    @Override
    protected void configure() {
    }
}
