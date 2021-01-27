package com.thebluehats.server.game.managers.enchants

interface GlobalTimerListener {
    fun onTimeStep()
    val tickDelay: Long
}