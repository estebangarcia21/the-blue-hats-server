package com.thebluehats.server.game.managers.enchants.processedevents

import org.bukkit.event.Event

open class CastedEvent<E : Event?>(val event: E)