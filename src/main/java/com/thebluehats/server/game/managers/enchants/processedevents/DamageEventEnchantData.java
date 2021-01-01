package com.thebluehats.server.game.managers.enchants.processedevents;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageEventEnchantData extends CastedEntityDamageByEntityEvent {
    private final ImmutableMap<Material, Integer> levelMap;

    public DamageEventEnchantData(EntityDamageByEntityEvent event, Player damager, Player damagee, ImmutableMap<Material, Integer> levelMap) {
        super(event, damager, damagee);

        this.levelMap = levelMap;
    }

    public int getLevel() {
        return levelMap.values().asList().get(0);
    }

    public ImmutableMap<Material, Integer> getLevelMap() {
        return levelMap;
    }
}
