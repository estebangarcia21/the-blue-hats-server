package me.stevemmmmm.server.game.enchants.templates;

import java.util.function.Consumer;
import java.util.function.Function;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.stevemmmmm.server.game.enchants.CustomEnchant;

public interface EventTemplate {
    boolean run(CustomEnchant enchant, Entity damager, Entity damagee, Function<PlayerInventory, ItemStack> getSource,
                Consumer<Integer> onSuccess);
}
