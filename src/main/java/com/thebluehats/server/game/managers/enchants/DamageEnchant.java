package com.thebluehats.server.game.managers.enchants;

import com.thebluehats.server.game.enchants.processedevents.PostEventTemplateResult;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public interface DamageEnchant extends CustomEnchant, PostEventExecutor<PostEventTemplateResult> {
    @EventHandler
    void onEntityDamageEntity(EntityDamageByEntityEvent event);
}
