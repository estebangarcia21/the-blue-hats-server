package com.thebluehats.server.core.services

import com.google.inject.Inject
import com.google.inject.Injector
import com.thebluehats.server.api.listeners.RegisterPlayer
import org.bukkit.plugin.java.JavaPlugin

class APIService @Inject constructor(private val plugin: JavaPlugin) : Service {
    override fun provision(injector: Injector) {
        plugin.server.pluginManager.registerEvents(injector.getInstance(RegisterPlayer::class.java), plugin)
    }
}