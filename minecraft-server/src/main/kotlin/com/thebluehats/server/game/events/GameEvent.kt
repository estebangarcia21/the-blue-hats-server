package com.thebluehats.server.game.events

interface GameEvent {
    val duration: Long
    fun onStart()
    fun onEnd()
}
