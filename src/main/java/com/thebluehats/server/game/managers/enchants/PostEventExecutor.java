package com.thebluehats.server.game.managers.enchants;

import com.thebluehats.server.game.managers.enchants.processedevents.TemplateResult;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public interface PostEventExecutor<E extends Event, T extends TemplateResult<?>> extends Listener {
    void onEvent(E event);

    void execute(T data);
}
