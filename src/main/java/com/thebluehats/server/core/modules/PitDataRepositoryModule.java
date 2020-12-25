package com.thebluehats.server.core.modules;

import java.util.UUID;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.api.implementations.pitdata.PitDataRepository;
import com.thebluehats.server.api.models.PitDataModel;
import com.thebluehats.server.api.utils.CRUDRepository;
import com.thebluehats.server.core.modules.annotations.PitDataProvider;

public class PitDataRepositoryModule extends AbstractModule {
    @Provides
    @Singleton
    @PitDataProvider
    static CRUDRepository<PitDataModel, UUID> providePitDataRepository() {
        return new PitDataRepository();
    }

    @Override
    protected void configure() {
    }
}
