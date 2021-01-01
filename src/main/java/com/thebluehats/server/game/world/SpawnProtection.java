package com.thebluehats.server.game.world;

import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerifier;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerifier;
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public class SpawnProtection {
    private final RegionManager regionManager;
    private final PlayerHitPlayerVerifier playerHitPlayerVerifier;
    private final ArrowHitPlayerVerifier arrowHitPlayerVerifier;

    public SpawnProtection(RegionManager regionManager, PlayerHitPlayerVerifier playerHitPlayerVerifier, ArrowHitPlayerVerifier arrowHitPlayerVerifier) {
        this.regionManager = regionManager;
        this.playerHitPlayerVerifier = playerHitPlayerVerifier;
        this.arrowHitPlayerVerifier = arrowHitPlayerVerifier;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (playerHitPlayerVerifier.verify(event) || arrowHitPlayerVerifier.verify(event)) {
            if (regionManager.entityIsInSpawn(event.getDamager()) || regionManager.entityIsInSpawn(event.getEntity())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {
        if (regionManager.entityIsInSpawn(event.getProjectile())) {
            event.setCancelled(true);
        }
    }
}
