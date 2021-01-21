package com.thebluehats.server.game.managers.combat.templates

import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.utils.EntityValidator
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import javax.inject.Inject

class ArrowDamageTrigger @Inject constructor(
    customEnchantUtils: CustomEnchantUtils,
    private val arrowHitPlayerVerifier: ArrowHitPlayerVerifier
) : DamageEnchantTrigger(customEnchantUtils) {
    override fun run(
        enchant: DamageTriggeredEnchant, event: EntityDamageByEntityEvent, targetPlayer: EnchantHolder,
        validators: Array<EntityValidator>
    ) {
        val damager = event.damager
        val damagee = event.entity
        if (!arrowHitPlayerVerifier.verify(event)) return
        val playerDamager = (damager as Arrow).shooter as Player
        val playerDamagee = damagee as Player
        val inventory = if (targetPlayer == EnchantHolder.DAMAGER) playerDamager.inventory else playerDamagee.inventory
        for (validator in validators) {
            if (!validator.validate(arrayOf(damager, damagee))) return
        }
        if (!inventoryHasEnchant(inventory, enchant)) return
        enchant.execute(DamageEventEnchantData(event, playerDamager, playerDamagee, getItemMap(enchant, inventory)))
    }
}