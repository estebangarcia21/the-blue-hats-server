package com.thebluehats.server.game.managers.combat.templates;

import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EntityValidator;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;

import javax.inject.Inject;

public class ArrowDamageTrigger extends DamageEnchantTrigger {
    private final ArrowHitPlayerVerifier arrowHitPlayerVerifier;

    @Inject
    public ArrowDamageTrigger(CustomEnchantUtils customEnchantUtils, ArrowHitPlayerVerifier arrowHitPlayerVerifier) {
        super(customEnchantUtils);

        this.arrowHitPlayerVerifier = arrowHitPlayerVerifier;
    }

    @Override
    public void run(DamageTriggeredEnchant enchant, EntityDamageByEntityEvent event, EnchantHolder targetPlayer,
                    EntityValidator... validators) {
        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();

        if (!arrowHitPlayerVerifier.verify(event)) return;

        Player playerDamager = (Player) ((Arrow) damager).getShooter();
        Player playerDamagee = (Player) damagee;

        PlayerInventory inventory = targetPlayer == EnchantHolder.DAMAGER ? playerDamager.getInventory() : playerDamagee.getInventory();

        for (EntityValidator validator : validators) {
            if (!validator.validate(damager, damagee))
                return;
        }

        if (!inventoryHasEnchant(inventory, enchant)) return;

        enchant.execute(new DamageEventEnchantData(event, playerDamager, playerDamagee, getItemMap(enchant, inventory)));
    }
}
