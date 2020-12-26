package com.thebluehats.server.game.managers.combat.templates;

import javax.inject.Inject;

import com.thebluehats.server.game.managers.enchants.processedevents.PostDamageEventTemplateResult;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.enchants.OnDamageEnchant;
import com.thebluehats.server.game.utils.EntityValidator;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;

public class ArrowHitPlayerTemplate extends PostDamageEventTemplate {
    @Inject
    public ArrowHitPlayerTemplate(CustomEnchantUtils customEnchantUtils) {
        super(customEnchantUtils);
    }

    @Override
    public void run(OnDamageEnchant enchant, EntityDamageByEntityEvent event, TargetPlayer targetPlayer,
            EntityValidator... validators) {
        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();

        if (damager instanceof Arrow && damagee instanceof Player) {
            Arrow arrow = (Arrow) damager;

            if (arrow.getShooter() instanceof Player) {
                Player playerDamager = (Player) damager;
                Player playerDamagee = (Player) damagee;

                PlayerInventory inventory = targetPlayer == TargetPlayer.DAMAGER ? playerDamager.getInventory()
                        : playerDamagee.getInventory();

                for (EntityValidator validator : validators) {
                    if (!validator.validate(damager, damagee))
                        return;
                }

                enchant.execute(new PostDamageEventTemplateResult(event, getItemMap(enchant, inventory), playerDamager,
                        playerDamagee));
            }
        }
    }
}
