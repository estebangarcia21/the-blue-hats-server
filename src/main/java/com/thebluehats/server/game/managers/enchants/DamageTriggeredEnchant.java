package com.thebluehats.server.game.managers.enchants;

import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;

import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EntityValidator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public abstract class DamageTriggeredEnchant implements CustomEnchant, PostEventExecutor<EntityDamageByEntityEvent, DamageEventEnchantData> {
    private final DamageEnchantTrigger[] triggers;
    private final EntityValidator[] validators;

    protected DamageTriggeredEnchant(DamageEnchantTrigger[] triggers) {
        this.triggers = triggers;
        this.validators = new EntityValidator[0];
    }

    protected DamageTriggeredEnchant(DamageEnchantTrigger[] triggers, EntityValidator[] validators) {
        this.triggers = triggers;
        this.validators = validators;
    }

    @Override
    @EventHandler
    public void onEvent(EntityDamageByEntityEvent event) {
        for (DamageEnchantTrigger trigger : triggers) {
            trigger.run(this, event, getEnchantHolder(), validators);
        }
    }

    public abstract EnchantHolder getEnchantHolder();
}
