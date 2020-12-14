package com.thebluehats.server.game.enchants.processedevents;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ProcessedEntityDamageByEntityEvent extends ProcessedEvent<EntityDamageByEntityEvent> {
    private final Player damager;
    private final Player damagee;

    public ProcessedEntityDamageByEntityEvent(EntityDamageByEntityEvent event, Player damager, Player damagee) {
        super(event);

        this.damager = damager;
        this.damagee = damagee;
    }

    public Player getDamager() {
        new ProcessedEvent<EntityDamageByEntityEvent>(null).getEvent();
        return damager;
    }

    public Player getDamagee() {
        return damagee;
    }
}
