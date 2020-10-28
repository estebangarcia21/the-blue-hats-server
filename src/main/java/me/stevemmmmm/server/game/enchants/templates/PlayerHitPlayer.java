package me.stevemmmmm.server.game.enchants.templates;

import java.util.function.Consumer;
import java.util.function.Function;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.stevemmmmm.server.game.enchants.CustomEnchant;

public class PlayerHitPlayer implements EventTemplate {
    @Override
    public boolean run(CustomEnchant enchant, Entity damager, Entity damagee,
            Function<PlayerInventory, ItemStack> getSource, Consumer<Integer> onSuccess) {
        if (damager instanceof Player && damagee instanceof Player) {
            Player player = (Player) damagee;
            ItemStack source = getSource.apply(player.getInventory());

            if (!enchant.canExecuteEnchant(source, new Entity[] { damager, damagee }))
                return false;

            onSuccess.accept(enchant.getEnchantLevel(source));

            return true;
        }

        return false;
    }
}
