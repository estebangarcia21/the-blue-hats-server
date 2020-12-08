package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.core.modules.annotations.AllEventTemplates;
import com.thebluehats.server.core.modules.annotations.ArrowHitPlayer;
import com.thebluehats.server.core.modules.annotations.PlayerHitPlayer;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.EventTemplate;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;

public class EventTemplatesModule extends AbstractModule {
    @Provides
    @Singleton
    @AllEventTemplates
    static EventTemplate[] provideAllEventTemplates() {
        return new EventTemplate[] { new PlayerHitPlayerTemplate(), new ArrowHitPlayerTemplate() };
    }

    @Provides
    @Singleton
    @PlayerHitPlayer
    static EventTemplate[] providePlayerHitPlayerTemplate() {
        return new EventTemplate[] { new PlayerHitPlayerTemplate() };
    }

    @Provides
    @Singleton
    @ArrowHitPlayer
    static EventTemplate[] provideArrowHitPlayerTemplate() {
        return new EventTemplate[] { new ArrowHitPlayerTemplate() };
    }

    @Override
    protected void configure() {
    }
}
