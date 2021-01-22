package com.thebluehats.server.game.managers.enchants

import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EntityValidator
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

abstract class DamageTriggeredEnchant : CustomEnchant,
    PostEventExecutor<EntityDamageByEntityEvent, DamageEventEnchantData> {
    private val triggers: Array<DamageEnchantTrigger>
    private val validators: Array<EntityValidator>

    protected constructor(triggers: Array<DamageEnchantTrigger>) {
        this.triggers = triggers
        this.validators = emptyArray()
    }

    protected constructor(triggers: Array<DamageEnchantTrigger>, validators: Array<EntityValidator>) {
        this.triggers = triggers
        this.validators = validators;
    }

    @EventHandler
    override fun onEvent(event: EntityDamageByEntityEvent) {
        for (trigger in triggers) {
            trigger.run(this, event, enchantHolder, validators)
        }
    }

    abstract val enchantHolder: EnchantHolder
}