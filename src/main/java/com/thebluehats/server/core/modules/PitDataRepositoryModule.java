package com.thebluehats.server.core.modules;

import java.util.UUID;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.api.models.PitDataModel;
import com.thebluehats.server.api.repos.PitDataRepository;
import com.thebluehats.server.api.repos.Repository;

public class PitDataRepositoryModule extends AbstractModule {
    @Provides
    @Singleton
    static Repository<UUID, PitDataModel> providePitDataRepository() {
        return new PitDataRepository();
    }

    @Override
    protected void configure() {
    }
}
