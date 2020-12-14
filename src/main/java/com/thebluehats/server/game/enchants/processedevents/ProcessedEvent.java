package com.thebluehats.server.game.enchants.processedevents;

import org.bukkit.event.Event;

public class ProcessedEvent<E extends Event> {
    protected final E event;

    protected ProcessedEvent(E event) {
        this.event = event;
    }

    public E getEvent() {
        return event;
    }
}
