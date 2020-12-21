package com.thebluehats.server.game.managers.combat.templates;

import com.google.inject.Inject;
import com.thebluehats.server.game.enchants.processedevents.PostEventTemplateResult;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.enchants.DamageEnchant;
import com.thebluehats.server.game.utils.EntityValidator;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;

public class PlayerHitPlayerTemplate extends PostEventTemplate {
    @Inject
    public PlayerHitPlayerTemplate(CustomEnchantUtils customEnchantUtils) {
        super(customEnchantUtils);
    }

    @Override
    public void run(DamageEnchant enchant, EntityDamageByEntityEvent event, TargetPlayer targetPlayer,
            EntityValidator... validators) {
        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();

        if (damager instanceof Player && damagee instanceof Player) {
            Player playerDamagee = (Player) damagee;
            Player playerDamager = (Player) damager;

            PlayerInventory inventory = targetPlayer == TargetPlayer.DAMAGER ? playerDamager.getInventory()
                    : playerDamagee.getInventory();

            for (EntityValidator validator : validators) {
                if (!validator.validate(damager, damagee)) {
                    return;
                }
            }

            enchant.execute(
                    new PostEventTemplateResult(event, getItemMap(enchant, inventory), playerDamager, playerDamagee));
        }
    }
}
