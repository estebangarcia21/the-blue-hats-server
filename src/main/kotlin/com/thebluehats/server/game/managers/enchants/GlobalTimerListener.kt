package com.thebluehats.server.game.managers.enchants

import org.bukkit.entity.Player

interface GlobalTimerListener {
    fun onTick(player: Player)
    val tickDelay: Long
}