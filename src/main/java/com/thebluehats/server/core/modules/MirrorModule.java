package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.core.modules.annotations.MirrorReference;
import com.thebluehats.server.game.enchants.Mirror;

public class MirrorModule extends AbstractModule {
    @Provides
    @Singleton
    @MirrorReference
    static Mirror provideMirror() {
        return new Mirror();
    }

    @Override
    protected void configure() {
    }
}
