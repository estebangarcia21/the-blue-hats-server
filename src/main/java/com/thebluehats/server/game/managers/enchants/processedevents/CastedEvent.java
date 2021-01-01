package com.thebluehats.server.game.managers.enchants.processedevents;

import org.bukkit.event.Event;

public class CastedEvent<E extends Event> {
    private final E event;

    public CastedEvent(E event) {
        this.event = event;
    }

    public E getEvent() {
        return event;
    }
}
