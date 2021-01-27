package com.thebluehats.server.game.managers.enchants

import com.google.inject.Inject
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.ArrayList

class GlobalPlayerTimer @Inject constructor(private val plugin: JavaPlugin) : GlobalTimer(plugin) {
    private val listeners = ArrayList<GlobalPlayerTimerListener>()
    private val times = ArrayList<Long>()

    fun addListener(listener: GlobalPlayerTimerListener) {
        listeners.add(listener)
    }

    override fun onPluginStart() {
        listeners.forEach { listener ->
            val tickDelay = listener.tickDelay

            if (!times.contains(tickDelay)) {
                times.add(tickDelay)

                Bukkit.getServer().scheduler.scheduleSyncRepeatingTask(plugin, {
                    Bukkit.getOnlinePlayers().forEach { player ->
                        if (listener.tickDelay == tickDelay) {
                            listener.onTimeStep(player)
                        }
                    }
                }, 0L, tickDelay)
            }
        }
    }
}