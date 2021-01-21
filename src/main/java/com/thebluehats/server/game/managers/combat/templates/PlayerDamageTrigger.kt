package com.thebluehats.server.game.managers.combat.templates

import com.google.inject.Inject
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EntityValidator
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class PlayerDamageTrigger @Inject constructor(
    customEnchantUtils: CustomEnchantUtils,
    private val playerHitPlayerVerifier: PlayerHitPlayerVerifier
) : DamageEnchantTrigger(customEnchantUtils) {

    override fun run(
        enchant: DamageTriggeredEnchant, event: EntityDamageByEntityEvent, targetPlayer: EnchantHolder,
        validators: Array<EntityValidator>
    ) {
        val damager = event.damager
        val damagee = event.entity
        if (!playerHitPlayerVerifier.verify(event)) return
        val playerDamager = damager as Player
        val playerDamagee = damagee as Player
        val inventory = if (targetPlayer == EnchantHolder.DAMAGER) playerDamager.inventory else playerDamagee.inventory
        for (validator in validators) {
            if (!validator.validate(damager, damagee)) {
                return
            }
        }
        if (!inventoryHasEnchant(inventory, enchant)) return
        enchant.execute(DamageEventEnchantData(event, playerDamager, playerDamagee, getItemMap(enchant, inventory)))
    }
}