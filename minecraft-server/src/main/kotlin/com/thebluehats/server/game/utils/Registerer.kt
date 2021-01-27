package com.thebluehats.server.game.utils

interface Registerer<T> {
    fun register(vararg objects: T)
}