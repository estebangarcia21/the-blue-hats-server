package com.thebluehats.server.game.managers.combat.templates;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EntityValidator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;

public class PlayerDamageTrigger extends DamageEnchantTrigger {
    private final PlayerHitPlayerVerifier playerHitPlayerVerifier;

    @Inject
    public PlayerDamageTrigger(CustomEnchantUtils customEnchantUtils, PlayerHitPlayerVerifier playerHitPlayerVerifier) {
        super(customEnchantUtils);

        this.playerHitPlayerVerifier = playerHitPlayerVerifier;
    }

    @Override
    public void run(DamageTriggeredEnchant enchant, EntityDamageByEntityEvent event, EnchantHolder targetPlayer,
                    EntityValidator... validators) {
        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();

        if (!playerHitPlayerVerifier.verify(event)) return;

        Player playerDamager = (Player) damager;
        Player playerDamagee = (Player) damagee;

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
