package com.thebluehats.server.game.managers.combat;

import java.util.UUID;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerifier;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerVerifier;
import com.thebluehats.server.game.managers.enchants.Timer;
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class CombatManager implements Listener {
    private final RegionManager regionManager;
    private final Timer<UUID> timer;
    private final PlayerHitPlayerVerifier playerHitPlayerVerifier;
    private final ArrowHitPlayerVerifier arrowHitPlayerVerifier;

    @Inject
    public CombatManager(RegionManager regionManager, Timer<UUID> timer, PlayerHitPlayerVerifier playerHitPlayerVerifier, ArrowHitPlayerVerifier arrowHitPlayerVerifier) {
        this.regionManager = regionManager;
        this.timer = timer;
        this.playerHitPlayerVerifier = playerHitPlayerVerifier;
        this.arrowHitPlayerVerifier = arrowHitPlayerVerifier;
    }


    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (playerHitPlayerVerifier.verify(event)) {
            combatTag((Player) event.getDamager());
            combatTag((Player) event.getEntity());

            return;
        }

        if (arrowHitPlayerVerifier.verify(event)) {
            combatTag((Player) ((Arrow) event.getDamager()).getShooter());
            combatTag((Player) event.getEntity());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        removePlayerFromCombat(event.getEntity());
    }

    public boolean playerIsInCombat(Player player) {
        return timer.isRunning(player.getUniqueId());
    }

    public void combatTag(Player player) {
        if (regionManager.entityIsInSpawn(player))return;

        UUID uuid = player.getUniqueId();

        timer.start(uuid, calculateCombatTime() * 20L, true);
    }

    public long getCombatTime(Player player) {
        return timer.getTime(player.getUniqueId()) / 20;
    }

    private int calculateCombatTime() {
        /*
         * In reality, bounty time should be calculated here but it will be implemented
         * in the future when bounties are added to the project
         */
        return 15;
    }

    private void removePlayerFromCombat(Player player) {
        timer.cancel(player.getUniqueId());
    }

    public CombatStatus getStatus(Player player) {
        return playerIsInCombat(player) ? CombatStatus.COMBAT : CombatStatus.IDLING;
    }
}