package com.thebluehats.server.game.managers.enchants

import org.bukkit.entity.Player

interface GlobalPlayerTimerListener {
    fun onTimeStep(player: Player)
    val tickDelay: Long
}