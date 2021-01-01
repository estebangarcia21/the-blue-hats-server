package com.thebluehats.server.game.managers.combat.templates;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ArrowHitPlayerVerifier implements EventVerifier<EntityDamageByEntityEvent>  {
    @Override
    public boolean verify(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();

        if (damager instanceof Arrow && damagee instanceof Player) {
            Arrow arrow = (Arrow) damager;

            return arrow.getShooter() instanceof Player;
        }

        return false;
    }
}
