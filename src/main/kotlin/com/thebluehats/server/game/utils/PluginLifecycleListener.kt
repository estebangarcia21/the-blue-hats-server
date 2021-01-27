package com.thebluehats.server.game.utils

interface PluginLifecycleListener {
    fun onPluginStart()
    fun onPluginEnd()
}