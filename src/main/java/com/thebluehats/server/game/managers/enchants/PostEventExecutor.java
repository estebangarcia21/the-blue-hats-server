package com.thebluehats.server.game.managers.enchants;

import com.thebluehats.server.game.managers.enchants.processedevents.TemplateResult;

import org.bukkit.event.Listener;

public interface PostEventExecutor<T extends TemplateResult<?>> extends Listener {
    void execute(T data);
}
