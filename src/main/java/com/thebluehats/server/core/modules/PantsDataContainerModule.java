package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.utils.PantsData;

public class PantsDataContainerModule extends AbstractModule {
    @Provides
    @Singleton
    static PantsData providePantsData() {
        return new PantsData();
    }

    @Override
    protected void configure() {
    }
}
