package com.thebluehats.server.game.managers.enchants.processedevents;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CastedEntityDamageByEntityEvent extends CastedEvent<EntityDamageByEntityEvent> {
    private final Player damager;
    private final Player damagee;

    public CastedEntityDamageByEntityEvent(EntityDamageByEntityEvent event, Player damager, Player damagee) {
        super(event);

        this.damager = damager;
        this.damagee = damagee;
    }

    public Player getDamager() {
        return damager;
    }

    public Player getDamagee() {
        return damagee;
    }
}
