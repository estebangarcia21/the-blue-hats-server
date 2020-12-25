package com.thebluehats.server.game.commands;

import java.util.Map;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnchantCommand extends GameCommand {
    private final CustomEnchantManager customEnchantManager;
    private final CustomEnchantUtils customEnchantUtils;

    @Inject
    public EnchantCommand(CustomEnchantManager customEnchantManager, CustomEnchantUtils customEnchantUtils) {
        this.customEnchantManager = customEnchantManager;
        this.customEnchantUtils = customEnchantUtils;
    }

    @Override
    public String[] getCommandNames() {
        return new String[] { "pitenchant" };
    }

    @Override
    public String getUsageMessage(String cmd) {
        return formatStandardUsageMessage(cmd, "Enchants an item with a Custom Enchant.", "enchantName", "level");
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

            return;
        }

        if (args.length < 2) {
            player.sendMessage(formatStandardErrorMessage("You did not specify a level!"));

            return;
        }

        if (!StringUtils.isNumeric(args[1])) {
            player.sendMessage(formatStandardErrorMessage("The level you entered is not a number!"));

            return;
        }

        if (customEnchantUtils.itemHasEnchant(customEnchant, item)) {
            player.sendMessage(formatStandardErrorMessage("This item already contains that enchant!"));

            return;
        }

        if (!customEnchantUtils.isCompatibleWith(customEnchant, item.getType())) {
            player.sendMessage(formatStandardErrorMessage("You can not enchant this that on this item!"));

            return;
        }

        if (Integer.parseInt(args[1]) > 3 || Integer.parseInt(args[1]) < 1) {
            player.sendMessage(formatStandardErrorMessage("The enchant level can only be 1, 2, or 3!"));

            return;
        }

        int level = Integer.parseInt(args[1]);

        if (player.getWorld().getName().equals("ThePit_0")) {
            int numberOfEnchants = customEnchantManager.getItemEnchants(item).size();

            if (numberOfEnchants >= 3) {
                player.sendMessage(
                        formatStandardErrorMessage("You can only have a maximum of 3 enchants in this world!"));

                return;
            }

            int tokens = customEnchantManager.getTokensOnItem(item) + level;

            if (tokens > 8) {
                player.sendMessage("You can only have a miximum of 8 tokens in this world!");

                return;
            }

            int rareTokens = 0;
            int rareEnchantCount = 0;

            for (Map.Entry<CustomEnchant, Integer> entry : customEnchantManager.getItemEnchants(item).entrySet()) {
                if (entry.getKey().isRareEnchant()) {
                    rareTokens += entry.getValue();
                    rareEnchantCount++;
                }
            }

            if (customEnchant.isRareEnchant()) {
                rareTokens += level;
                rareEnchantCount++;
            }

            if (rareEnchantCount > 2) {
                player.sendMessage(
                        formatStandardErrorMessage("You can only have 2 rare enchants on an item in this world!"));

                return;
            }

            if (rareTokens > 4) {
                player.sendMessage(formatStandardErrorMessage(
                        "You can only have a maximum of 4 tokens for rare enchants in this world!"));

                return;
            }
        }

        customEnchantManager.addEnchant(item, level, false, customEnchant);

        player.sendMessage(
                ChatColor.DARK_PURPLE + "Success!" + ChatColor.RED + " You applied the enchantment successfully!");
        player.updateInventory();
    }
}
