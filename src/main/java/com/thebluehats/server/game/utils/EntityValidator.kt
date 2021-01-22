package com.thebluehats.server.game.utils

import org.bukkit.entity.Entity

interface EntityValidator {
    fun validate(vararg entities: Entity): Boolean
}