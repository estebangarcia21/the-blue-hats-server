package com.thebluehats.server.game.enchants.processedevents;

import org.bukkit.event.Event;

public class TemplateResult<E extends Event> {
    protected final E event;

    protected TemplateResult(E event) {
        this.event = event;
    }

    public E getEvent() {
        return event;
    }
}
