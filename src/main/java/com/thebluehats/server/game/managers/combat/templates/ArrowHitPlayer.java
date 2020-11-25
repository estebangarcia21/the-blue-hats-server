package com.thebluehats.server.game.managers.combat.templates;

import java.util.function.Consumer;
import java.util.function.Function;

import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ArrowHitPlayer implements EventTemplate {
    public boolean run(CustomEnchant<?> enchant, Entity damager, Entity damagee, Function<PlayerInventory, ItemStack> getSource, Consumer<Integer> onSuccess) {
        if (damager instanceof Arrow && damagee instanceof Player) {
            Arrow arrow = (Arrow) damager;

            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) damagee;
                ItemStack source = getSource.apply(player.getInventory());

                if (!enchant.canExecuteEnchant(source, new Entity[] { damager, damagee })) return false;

                onSuccess.accept(enchant.getEnchantLevel(source));

                return true;
            }
        }

        return false;
    }
}
