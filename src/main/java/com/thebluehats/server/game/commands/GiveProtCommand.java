package com.thebluehats.server.game.commands;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveProtCommand extends GameCommand {
    @Override
    public String[] getCommandNames() {
        return new String[] { "giveprot" };
    }

    @Override
    public String getUsageMessage(String cmd) {
        return formatStandardUsageMessage(cmd, "Gives a prot 1 set.");
    }

    @Override
    public void runCommand(Player player, String commandName, String[] args) {
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        addProtectionOne(helmet);

        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        addProtectionOne(chestplate);

        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        addProtectionOne(leggings);

        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        addProtectionOne(boots);

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta swordMeta = sword.getItemMeta();

        swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        swordMeta.setUnbreakable(true);

        sword.setItemMeta(swordMeta);

        player.getInventory().addItem(helmet, chestplate, leggings, boots, sword);
    }

    private void addProtectionOne(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        meta.setUnbreakable(true);

        item.setItemMeta(meta);
    }
}
