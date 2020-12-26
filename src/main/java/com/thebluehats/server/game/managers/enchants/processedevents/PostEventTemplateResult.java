package com.thebluehats.server.game.managers.enchants.processedevents;

import com.google.common.collect.ImmutableMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PostEventTemplateResult extends TemplateResult<EntityDamageByEntityEvent> {
    private final Player damager;
    private final Player damagee;

    public PostEventTemplateResult(EntityDamageByEntityEvent event, ImmutableMap<Material, Integer> levelMap,
            Player damager, Player damagee) {
        super(event, levelMap);

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
