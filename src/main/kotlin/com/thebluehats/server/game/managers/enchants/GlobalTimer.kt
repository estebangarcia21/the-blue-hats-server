package com.thebluehats.server.game.managers.enchants

import com.thebluehats.server.game.utils.PluginLifecycleListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import javax.inject.Inject

class GlobalTimer @Inject constructor(private val plugin: JavaPlugin) : PluginLifecycleListener {
    private val listeners = ArrayList<GlobalTimerListener>()
    private val times = ArrayList<Long>()
    fun addListener(listener: GlobalTimerListener) {
        listeners.add(listener)
    }

    override fun onPluginStart() {
        for (globalTimerListener in listeners) {
            val tickDelay = globalTimerListener.tickDelay
            if (!times.contains(tickDelay)) {
                times.add(tickDelay)
                Bukkit.getServer().scheduler.scheduleSyncRepeatingTask(plugin, {
                    for (player in Bukkit.getOnlinePlayers()) {
                        for (listener in listeners) {
                            if (listener.tickDelay == tickDelay) {
                                listener.onTick(player)
                            }
                        }
                    }
                }, 0L, tickDelay)
            }
        }
        //
//        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
//            for (Player player : Bukkit.getOnlinePlayers()) {
//                for (GlobalTimerListener listener : listeners) {
//                    listener.onTick(player);
//                }
//            }
//        }, 0L, 1L);
//
//        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
//            for (Player player : Bukkit.getOnlinePlayers()) {
//                for (GlobalTimerListener listener : listeners) {
//                    listener.onTick(player);
//                }
//            }
//        }, 0L, 20L);
    }

    override fun onPluginEnd() {}
}