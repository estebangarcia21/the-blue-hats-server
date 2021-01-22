package com.thebluehats.server.game.managers.combat.templates

import org.bukkit.event.Event

interface EventVerifier<E : Event?> {
    fun verify(event: E): Boolean
}