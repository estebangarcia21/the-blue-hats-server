package com.thebluehats.server.game.managers.combat

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerifier
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerifier
import com.thebluehats.server.game.managers.enchants.Timer
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import java.util.*

class CombatManager @Inject constructor(
    private val regionManager: RegionManager,
    private val timer: Timer<UUID>,
    private val playerHitPlayerVerifier: PlayerHitPlayerVerifier,
    private val arrowHitPlayerVerifier: ArrowHitPlayerVerifier
) : Listener {
    @EventHandler
    fun onPlayerHit(event: EntityDamageByEntityEvent) {
        if (playerHitPlayerVerifier.verify(event)) {
            combatTag(event.damager as Player)
            combatTag(event.entity as Player)
            return
        }

        if (arrowHitPlayerVerifier.verify(event)) {
            combatTag((event.damager as Arrow).shooter as Player)
            combatTag(event.entity as Player)
        }
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        removePlayerFromCombat(event.entity)
    }

    fun playerIsInCombat(player: Player): Boolean {
        return timer.isRunning(player.uniqueId)
    }

    fun combatTag(player: Player) {
        if (regionManager.entityIsInSpawn(player)) return
        val uuid = player.uniqueId
        timer.start(uuid, calculateCombatTime() * 20L, true)
    }

    fun getCombatTime(player: Player): Long {
        return timer.getTime(player.uniqueId) / 20
    }

    private fun calculateCombatTime(): Int {
        /*
         * In reality, bounty time should be calculated here but it will be implemented
         * in the future when bounties are added to the project
         */
        return 15
    }

    private fun removePlayerFromCombat(player: Player) {
        timer.cancel(player.uniqueId)
    }

    fun getStatus(player: Player): CombatStatus {
        return if (playerIsInCombat(player)) CombatStatus.COMBAT else CombatStatus.IDLING
    }
}