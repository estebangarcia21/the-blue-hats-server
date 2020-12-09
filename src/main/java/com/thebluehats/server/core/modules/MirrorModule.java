package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.core.modules.annotations.AllEventTemplates;
import com.thebluehats.server.core.modules.annotations.MirrorReference;
import com.thebluehats.server.game.enchants.Mirror;
import com.thebluehats.server.game.managers.combat.templates.EventTemplate;

public class MirrorModule extends AbstractModule {
    @Provides
    @Singleton
    @MirrorReference
    static Mirror provideMirror(@AllEventTemplates EventTemplate[] templates) {
        return new Mirror(templates);
    }

    @Override
    protected void configure() {
    }
}
