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
    private val validators: Array<EntityValidator?>

    protected constructor(triggers: Array<DamageEnchantTrigger>) {
        this.triggers = triggers
        this.validators = arrayOfNulls(0)
    }

    protected constructor(triggers: Array<DamageEnchantTrigger>, validators: Array<EntityValidator?>) {
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

    protected infix fun Array<Array<String>?>.add(variable: Var) {
        this[variable.index] = arrayOf(variable.one, variable.two, variable.three)
    }

    protected fun varMatrix(): Array<Array<String>?> = arrayOfNulls(2)

    protected class Var(val index: Int, val one: String, val two: String, val three: String)
}