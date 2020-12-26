package com.thebluehats.server.game.managers.enchants;

import com.thebluehats.server.game.managers.combat.templates.PostDamageEventTemplate;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.enchants.processedevents.PostDamageEventResult;

import com.thebluehats.server.game.utils.EntityValidator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public abstract class OnDamageEnchant implements CustomEnchant, PostEventExecutor<EntityDamageByEntityEvent, PostDamageEventResult> {
    private final PostDamageEventTemplate[] templates;
    private final EntityValidator[] validators;

    protected OnDamageEnchant(PostDamageEventTemplate[] templates) {
        this.templates = templates;
        this.validators = new EntityValidator[0];
    }

    protected OnDamageEnchant(PostDamageEventTemplate[] templates, EntityValidator[] validators) {
        this.templates = templates;
        this.validators = validators;
    }

    @Override
    @EventHandler
    public void onEvent(EntityDamageByEntityEvent event) {
        for (PostDamageEventTemplate template : templates) {
            template.run(this, event, getEnchantHolder(), validators);
        }
    }

    public abstract EnchantHolder getEnchantHolder();
}
