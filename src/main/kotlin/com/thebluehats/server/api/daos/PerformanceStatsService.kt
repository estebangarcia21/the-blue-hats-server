package com.thebluehats.server.api.daos

import org.bukkit.entity.Player

interface PerformanceStatsService {
    fun getPlayerXp(player: Player): Int
    fun setPlayerXp(player: Player, value: Int)
    fun getPlayerGold(player: Player): Double
    fun setPlayerGold(player: Player, value: Double)
}