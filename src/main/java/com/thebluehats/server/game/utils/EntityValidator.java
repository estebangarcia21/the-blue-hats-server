package com.thebluehats.server.game.utils;

import org.bukkit.entity.Entity;

public interface EntityValidator {
    boolean validate(Entity... entities);
}
