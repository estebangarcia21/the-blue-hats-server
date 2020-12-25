package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.utils.PantsDataContainer;

public class PantsDataContainerModule extends AbstractModule {
    @Provides
    @Singleton
    static PantsDataContainer providePantsData() {
        return new PantsDataContainer();
    }

    @Override
    protected void configure() {
    }
}
