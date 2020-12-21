package com.thebluehats.server.game.enchants.processedevents;

import com.google.common.collect.ImmutableMap;

import org.bukkit.Material;
import org.bukkit.event.Event;

public class TemplateResult<E extends Event> {
    private final E event;
    private final ImmutableMap<Material, Integer> levelMap;

    public TemplateResult(E event, ImmutableMap<Material, Integer> levelMap) {
        this.levelMap = levelMap;
        this.event = event;
    }

    public E getEvent() {
        return event;
    }

    public int getPrimaryLevel() {
        return levelMap.values().asList().get(0);
    }

    public ImmutableMap<Material, Integer> getLevelMap() {
        return levelMap;
    }
}
