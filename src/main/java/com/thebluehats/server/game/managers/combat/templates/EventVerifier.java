package com.thebluehats.server.game.managers.combat.templates;

import org.bukkit.event.Event;

public interface EventVerifier<E extends Event> {
    boolean verify(E event);
}
