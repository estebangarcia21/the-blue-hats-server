package com.thebluehats.server.game.managers.combat.templates;

import java.util.function.Function;

import javax.inject.Inject;

import com.thebluehats.server.game.enchants.processedevents.ProcessedEntityDamageByEntityEvent;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.enchants.DamageEnchant;
import com.thebluehats.server.game.utils.EntityValidator;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ArrowHitPlayerTemplate
        extends PostEventTemplate<EntityDamageByEntityEvent, ProcessedEntityDamageByEntityEvent> {
    @Inject
    public ArrowHitPlayerTemplate(CustomEnchantUtils customEnchantUtils) {
        super(customEnchantUtils);
    }

    @Override
    void run(DamageEnchant enchant, EntityDamageByEntityEvent event, Function<PlayerInventory, ItemStack> getSource,
            EntityValidator... validators) {
        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();

        if (damager instanceof Arrow && damagee instanceof Player) {
            Arrow arrow = (Arrow) damager;

            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) damagee;
                ItemStack source = getSource.apply(player.getInventory());

                for (EntityValidator validator : validators) {
                    if (!validator.validate(damager, damagee))
                        return;
                }

                enchant.execute(new ProcessedEntityDamageByEntityEvent(event, (Player) damager, (Player) damagee),
                        customEnchantUtils.getEnchantLevel(enchant, source));
            }
        }
    }
}
