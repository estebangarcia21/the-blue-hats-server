package com.thebluehats.server.game.managers.enchants

import com.google.inject.Inject
import org.bukkit.entity.Player
import java.util.*

class HitCounter @Inject constructor(private val timer: Timer<UUID>) {
    private val timerData = HashMap<UUID, HitCounterData>()

    fun addOne(player: Player) {
        val data = timerData.computeIfAbsent(player.uniqueId) { HitCounterData() }
        data.hitResetTime = 0L
        data.hitsWithEnchant = data.hitsWithEnchant + 1
        startHitResetTimer(player)
    }

    fun add(player: Player, amount: Int) {
        val data = timerData.computeIfAbsent(player.uniqueId) { HitCounterData() }
        data.hitResetTime = 0L
        data.hitsWithEnchant = data.hitsWithEnchant + amount
        startHitResetTimer(player)
    }

    fun hasHits(player: Player, hitAmount: Int): Boolean {
        val data = timerData.computeIfAbsent(player.uniqueId) { HitCounterData() }

        if (data.hitsWithEnchant >= hitAmount) {
            data.hitsWithEnchant = 0
            return true
        }

        return false
    }

    fun startHitResetTimer(player: Player) {
        val data = timerData.computeIfAbsent(player.uniqueId) { HitCounterData() }
        timer.start(player.uniqueId, (3 * 20).toLong(), true) { data.hitsWithEnchant = 0 }
    }

    private class HitCounterData {
        var hitsWithEnchant = 0
        var hitResetTime: Long = 0
    }
}