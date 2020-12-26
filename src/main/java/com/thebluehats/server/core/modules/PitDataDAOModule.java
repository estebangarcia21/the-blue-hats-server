package com.thebluehats.server.core.modules;

import java.util.UUID;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.api.models.PitDataModel;
import com.thebluehats.server.api.utils.CrudRepository;
import com.thebluehats.server.core.modules.annotations.PitDataProvider;
import com.thebluehats.server.core.modules.annotations.ServerApi;
import com.thebluehats.server.api.daos.PitDataDao;
import com.thebluehats.server.api.implementations.pitdata.PitDataDaoImpl;

import kong.unirest.UnirestInstance;

public class PitDataDAOModule extends AbstractModule {
    @Provides
    @Singleton
    static PitDataDao provideGrindingSystem(@ServerApi UnirestInstance serverApi,
            @PitDataProvider CrudRepository<PitDataModel, UUID> pitDataRepository) {
        return new PitDataDaoImpl(serverApi, pitDataRepository);
    }

    @Override
    protected void configure() {
    }
}
