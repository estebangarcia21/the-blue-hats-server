package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.thebluehats.server.core.TheBlueHatsServerPlugin
import org.bukkit.plugin.java.JavaPlugin

class PluginModule(private val plugin: TheBlueHatsServerPlugin) : AbstractModule() {
    override fun configure() {
        bind(JavaPlugin::class.java).toInstance(plugin)
        bind(TheBlueHatsServerPlugin::class.java).toInstance(plugin)
    }
}