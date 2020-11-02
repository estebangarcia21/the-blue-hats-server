package com.thebluehats.server.game.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveProtCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("giveprot")) {
                ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
                enchantToProt(helmet);

                ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
                enchantToProt(chestplate);

                ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
                enchantToProt(leggings);

                ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
                enchantToProt(boots);

                ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
                ItemMeta swordMeta = sword.getItemMeta();

                swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                swordMeta.setUnbreakable(true);

                sword.setItemMeta(swordMeta);

                player.getInventory().addItem(helmet, chestplate, leggings, boots, sword);
            }
        }

        return true;
    }

    private void enchantToProt(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        meta.setUnbreakable(true);

        item.setItemMeta(meta);
    }
}
