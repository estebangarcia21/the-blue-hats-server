package com.thebluehats.server.core.modules;

import java.util.UUID;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.api.models.PitDataModel;
import com.thebluehats.server.api.utils.CrudRepository;
import com.thebluehats.server.core.modules.annotations.PitDataProvider;

public class PitDataRepositoryModule extends AbstractModule {
    @Provides
    @Singleton
    @PitDataProvider
    static CrudRepository<PitDataModel, UUID> providePitDataRepository() {
        return new CrudRepository<>(PitDataModel::getPlayerUuid);
    }

    @Override
    protected void configure() {
    }
}
