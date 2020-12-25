package com.thebluehats.server.game.commands;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UnenchantCommand extends GameCommand {
    private final CustomEnchantManager customEnchantManager;
    private final CustomEnchantUtils customEnchantUtils;

    @Inject
    public UnenchantCommand(CustomEnchantManager customEnchantManager, CustomEnchantUtils customEnchantUtils) {
        this.customEnchantManager = customEnchantManager;
        this.customEnchantUtils = customEnchantUtils;
    }

    @Override
    public String[] getCommandNames() {
        return new String[] { "unenchant" };
    }

    @Override
    public String getUsageMessage(String cmd) {
        return formatStandardUsageMessage(cmd, "Unenchants an enchant from the item you are holding.", "enchantName");
    }

    @Override
    public void runCommand(Player player, String commandName, String[] args) {
        CustomEnchant customEnchant = null;

        for (CustomEnchant enchant : customEnchantManager.getEnchants()) {
            if (enchant.getEnchantReferenceName().equalsIgnoreCase(args[0])) {
                customEnchant = enchant;
            }
        }

        if (customEnchant == null) {
            player.sendMessage(formatStandardErrorMessage("This enchant does not exist!"));

            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) {
            player.sendMessage(formatStandardErrorMessage("You are not holding anything!"));
        } else if (args.length > 1) {
            player.sendMessage(formatStandardErrorMessage("Too many arguments"));
        } else if (item.getType() != Material.LEATHER_LEGGINGS && item.getType() != Material.GOLDEN_SWORD
                && item.getType() != Material.BOW) {
            player.sendMessage(formatStandardErrorMessage("You can not unenchant this item!"));
        } else if (!customEnchantUtils.itemHasEnchant(customEnchant, item)) {
            player.sendMessage(ChatColor.DARK_PURPLE + "Error!" + ChatColor.RED
                    + " This item does not have the specified enchant!");
        } else {
            customEnchantManager.removeEnchant(item, customEnchant);

            player.sendMessage(
                    ChatColor.DARK_PURPLE + "Success!" + ChatColor.RED + " You unenchanted the enchant successfully!");
        }
    }
}
