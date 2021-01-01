package com.thebluehats.server.game.world;

import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerificationTemplate;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerificationTemplate;
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public class SpawnProtection {
    private final RegionManager regionManager;
    private final PlayerHitPlayerVerificationTemplate playerHitPlayerVerificationTemplate;
    private final ArrowHitPlayerVerificationTemplate arrowHitPlayerVerificationTemplate;

    public SpawnProtection(RegionManager regionManager, PlayerHitPlayerVerificationTemplate playerHitPlayerVerificationTemplate, ArrowHitPlayerVerificationTemplate arrowHitPlayerVerificationTemplate) {
        this.regionManager = regionManager;
        this.playerHitPlayerVerificationTemplate = playerHitPlayerVerificationTemplate;
        this.arrowHitPlayerVerificationTemplate = arrowHitPlayerVerificationTemplate;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (playerHitPlayerVerificationTemplate.verify(event) || arrowHitPlayerVerificationTemplate.verify(event)) {
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
