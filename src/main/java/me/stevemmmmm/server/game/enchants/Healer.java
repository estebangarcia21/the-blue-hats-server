package me.stevemmmmm.server.game.enchants;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.stevemmmmm.server.game.utils.LoreBuilder;

public class Healer extends CustomEnchant {
    private final EnchantProperty<Integer> healAmount = new EnchantProperty<>(2, 4, 6);

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack source = player.getInventory().getItemInMainHand();
            int level = getEnchantLevel(source);

            if(!canExecuteEnchant(source, new Entity[] {event.getEntity(), event.getDamager() }))
                return;

            executeEnchant((Player) event.getDamager(), player, healAmount.getValueAtLevel(level));
        }
    }

    public void executeEnchant(Player damager, Player damaged, int healAmount) {
        damager.setHealth(Math.min(damager.getHealth() + healAmount, damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
        damaged.setHealth(Math.min(damaged.getHealth() + healAmount, damaged.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
    }

    @Override
    public String getName() {
        return "Healer";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Healer";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("1❤", "2❤", "3❤").write("Hitting a player ").setColor(ChatColor.GREEN)
                .write("heals").resetColor().write(" both you and them for ").setColor(ChatColor.RED)
                .writeVariable(0, level).build();
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return true;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.B;
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.GOLDEN_SWORD };
    }
}