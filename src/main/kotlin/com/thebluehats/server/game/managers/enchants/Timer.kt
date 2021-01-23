package com.thebluehats.server.game.managers.enchants

import com.google.inject.Inject
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class Timer<K> @Inject constructor(private val plugin: JavaPlugin) {
    private val timerData = HashMap<K, TimerData>()

    fun start(key: K, time: Long, resetTime: Boolean = false, seconds: Boolean = false, post: Runnable? = null) {
        val data = timerData.computeIfAbsent(key, { TimerData() })

        if (data.isRunning) {
            if (resetTime) data.time = if (seconds) time * 20 else time

            return
        }

        data.time = time

        data.taskId = Bukkit.getServer().scheduler.scheduleSyncRepeatingTask(plugin, {
            data.time = data.time - 1

            if (data.time <= 0) {
                data.time = 0
                Bukkit.getServer().scheduler.cancelTask(data.taskId)
                timerData.remove(key)

                post?.run()
            }
        }, 0L, 1L)
    }

    fun cancel(key: K) {
        val data = timerData.computeIfAbsent(key, { TimerData() })
        Bukkit.getServer().scheduler.cancelTask(data.taskId)
        timerData.remove(key)
    }

    val keys: Set<K>
        get() = timerData.keys

    fun isRunning(key: K): Boolean {
        val data = timerData[key]
        return data != null && data.isRunning
    }

    fun getTime(key: K): Long {
        val data = timerData[key]
        return data?.time ?: 0L
    }

    private class TimerData {
        var time: Long = 0
        var taskId = 0
        val isRunning: Boolean
            get() = time != 0L
    }
}