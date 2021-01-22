package com.thebluehats.server.game.perks

import org.bukkit.event.Listener

abstract class Perk : Listener {
    abstract val name: String
    abstract val cost: Int
}