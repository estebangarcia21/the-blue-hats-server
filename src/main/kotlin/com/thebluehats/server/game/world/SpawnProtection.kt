package com.thebluehats.server.game.world

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerifier
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerifier
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityShootBowEvent

class SpawnProtection @Inject constructor(
    private val regionManager: RegionManager,
    private val playerHitPlayerVerifier: PlayerHitPlayerVerifier,
    private val arrowHitPlayerVerifier: ArrowHitPlayerVerifier
) : Listener {
    @EventHandler
    fun onHit(event: EntityDamageByEntityEvent) {
        if (playerHitPlayerVerifier.verify(event) || arrowHitPlayerVerifier.verify(event)) {
            if (regionManager.entityIsInSpawn(event.damager) || regionManager.entityIsInSpawn(event.entity)) {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onArrowShoot(event: EntityShootBowEvent) {
        if (regionManager.entityIsInSpawn(event.projectile)) {
            event.isCancelled = true
        }
    }
}