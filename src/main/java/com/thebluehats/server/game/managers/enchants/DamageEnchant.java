package com.thebluehats.server.game.managers.enchants;

import com.thebluehats.server.game.managers.enchants.processedevents.PostEventTemplateResult;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

public interface DamageEnchant extends CustomEnchant, PostEventExecutor<PostEventTemplateResult> {
    void onEntityDamageEntity(EntityDamageByEntityEvent event);
}
