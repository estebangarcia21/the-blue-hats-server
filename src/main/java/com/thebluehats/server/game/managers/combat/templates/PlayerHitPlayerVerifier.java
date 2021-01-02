package com.thebluehats.server.game.managers.combat.templates;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerHitPlayerVerifier implements EventVerifier<EntityDamageByEntityEvent> {
    @Override
    public boolean verify(EntityDamageByEntityEvent event) {
        return event.getDamager() instanceof Player && event.getEntity() instanceof Player;
    }
}
