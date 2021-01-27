package com.thebluehats.server.game.managers.enchants

import com.thebluehats.server.game.utils.PluginLifecycleListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import javax.inject.Inject

open class GlobalTimer @Inject constructor(private val plugin: JavaPlugin) : PluginLifecycleListener {
    private val listeners = ArrayList<GlobalTimerListener>()
    private val times = ArrayList<Long>()

    fun addListener(listener: GlobalTimerListener) {
        listeners.add(listener)
    }

    override fun onPluginStart() {
        listeners.forEach { listener ->
            val tickDelay = listener.tickDelay

            if (!times.contains(tickDelay)) {
                times.add(tickDelay)

                Bukkit.getServer().scheduler.scheduleSyncRepeatingTask(plugin, {
                    if (listener.tickDelay == tickDelay) {
                        listener.onTimeStep()
                    }
                }, 0L, tickDelay)
            }
        }
    }

    override fun onPluginEnd() {}
}