package com.thebluehats.server.core

import com.thebluehats.server.core.services.*
import com.thebluehats.server.game.utils.PluginLifecycleListener
import com.thebluehats.server.game.utils.Registerer
import org.bukkit.plugin.java.JavaPlugin

class TheBlueHatsServerPlugin : JavaPlugin(), Registerer<PluginLifecycleListener> {
    val lifecycleListeners = arrayListOf<PluginLifecycleListener>()

    private var app: Application? = null

    override fun onEnable() {
        app = ApplicationBuilder(this)
            .addService(APIService::class.java)
            .addService(UtilitiesService::class.java)
            .addService(CustomEnchantService::class.java)
            .addService(PerksService::class.java)
            .addService(CommandsService::class.java)
            .build()

        app!!.start()
    }

    override fun onDisable() {
        app!!.stop()
    }

    override fun register(objects: Array<PluginLifecycleListener>) {
        lifecycleListeners.addAll(objects)
    }
}