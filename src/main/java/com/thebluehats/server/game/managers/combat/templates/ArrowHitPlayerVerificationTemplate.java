package com.thebluehats.server.game.managers.combat.templates;

import javax.inject.Inject;

import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EntityValidator;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;

public class ArrowHitPlayerVerificationTemplate extends DamageEventVerificationTemplate implements EventVerifier<EntityDamageByEntityEvent> {
    @Inject
    public ArrowHitPlayerVerificationTemplate(CustomEnchantUtils customEnchantUtils) {
        super(customEnchantUtils);
    }

    @Override
    public void run(DamageTriggeredEnchant enchant, EntityDamageByEntityEvent event, EnchantHolder targetPlayer,
                    EntityValidator... validators) {
        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();

        if (damager instanceof Arrow && damagee instanceof Player) {
            Arrow arrow = (Arrow) damager;

            if (arrow.getShooter() instanceof Player) {
                Player playerDamager = (Player) arrow.getShooter();
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
    }

    @Override
    public boolean verify(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();

        if (damager instanceof Arrow && damagee instanceof Player) {
            Arrow arrow = (Arrow) damager;

            return arrow.getShooter() instanceof Player;
        }

        return false;
    }
}
