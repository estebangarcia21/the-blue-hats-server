package com.thebluehats.server.game.managers.enchants;

import com.thebluehats.server.game.managers.enchants.processedevents.PostDamageEventTemplateResult;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

public interface OnDamageEnchant extends CustomEnchant, PostEventExecutor<PostDamageEventTemplateResult> {
    void onEntityDamageEntity(EntityDamageByEntityEvent event);
}
