package com.thebluehats.server.game.utils

interface Registerer<T> {
    fun register(objects: Array<T>)
}