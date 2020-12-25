package com.thebluehats.server.core.modules;

import java.util.UUID;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.api.models.PitDataModel;
import com.thebluehats.server.api.utils.CRUDRepository;
import com.thebluehats.server.core.modules.annotations.PitDataProvider;
import com.thebluehats.server.core.modules.annotations.ServerApi;
import com.thebluehats.server.api.implementations.pitdata.PitDataDAO;
import com.thebluehats.server.api.implementations.pitdata.PitDataDAOImpl;

import kong.unirest.UnirestInstance;

public class PitDataDAOModule extends AbstractModule {
    @Provides
    @Singleton
    static PitDataDAO provideGrindingSystem(@ServerApi UnirestInstance serverApi,
            @PitDataProvider CRUDRepository<PitDataModel, UUID> pitDataRepository) {
        return new PitDataDAOImpl(serverApi, pitDataRepository);
    }

    @Override
    protected void configure() {
    }
}
