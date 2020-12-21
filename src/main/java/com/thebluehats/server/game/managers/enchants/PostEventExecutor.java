package com.thebluehats.server.game.managers.enchants;

public interface PostEventExecutor<T> {
    void execute(T data);
}
