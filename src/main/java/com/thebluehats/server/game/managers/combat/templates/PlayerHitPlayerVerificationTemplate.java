package com.thebluehats.server.game.managers.combat.templates;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.processedevents.CastedEntityDamageByEntityEvent;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EntityValidator;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.function.Consumer;

public class PlayerHitPlayerVerificationTemplate extends DamageEventVerificationTemplate implements EventVerifier<EntityDamageByEntityEvent> {
    @Inject
    public PlayerHitPlayerVerificationTemplate(CustomEnchantUtils customEnchantUtils) {
        super(customEnchantUtils);
    }

    @Override
    public void run(DamageTriggeredEnchant enchant, EntityDamageByEntityEvent event, EnchantHolder targetPlayer,
                    EntityValidator... validators) {
        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();

        if (damager instanceof Player && damagee instanceof Player) {
            Player playerDamagee = (Player) damagee;
            Player playerDamager = (Player) damager;

            PlayerInventory inventory = targetPlayer == EnchantHolder.DAMAGER ? playerDamager.getInventory() : playerDamagee.getInventory();

            for (EntityValidator validator : validators) {
                if (!validator.validate(damager, damagee)) {
                    return;
                }
            }

            if (!inventoryHasEnchant(inventory, enchant)) return;

            enchant.execute(new DamageEventEnchantData(event, playerDamager, playerDamagee, getItemMap(enchant, inventory)));
        }
    }

    public void exec(EntityDamageByEntityEvent event, Consumer<CastedEntityDamageByEntityEvent> result) {
        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();

        if (damager instanceof Player && damagee instanceof Player) {
            Player playerDamagee = (Player) damagee;
            Player playerDamager = (Player) damager;
        }
    }

    @Override
    public boolean verify(EntityDamageByEntityEvent event) {
        return event.getDamager() instanceof Player && event.getEntity() instanceof Player;
    }
}
