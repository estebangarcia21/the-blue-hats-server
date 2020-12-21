package com.thebluehats.server.game.managers.enchants;

import org.bukkit.event.Listener;

public interface PostEventExecutor<T> extends Listener {
    void execute(T data);
}
