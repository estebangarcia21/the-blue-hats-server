package com.thebluehats.server.game.commands;

import java.util.Map;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdName = "pitenchant";

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase(cmdName)) {
                if (args.length == 0) {
                    sendUsageMessage(player, cmdName, "Enchants an item with a custom enchant.", "enchant-name",
                            "level");

                    return true;
                } else {
                    CustomEnchant customEnchant = null;

                    for (CustomEnchant enchant : customEnchantManager.getEnchants()) {
                        if (enchant.getEnchantReferenceName().equalsIgnoreCase(args[0])) {
                            customEnchant = enchant;
                        }
                    }

                    if (customEnchant == null) {
                        player.sendMessage(
                                ChatColor.DARK_PURPLE + "Error!" + ChatColor.RED + " This enchant does not exist!");
                        return true;
                    }

                    ItemStack item = player.getInventory().getItemInMainHand();

                    if (item.getType() == Material.AIR) {
                        player.sendMessage(
                                ChatColor.DARK_PURPLE + "Error!" + ChatColor.RED + " You are not holding anything!");
                        return true;
                    }

                    if (args.length < 2) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "Error!" + ChatColor.RED
                                + " You did not specify an enchantment level!");
                        return true;
                    }

                    if (!StringUtils.isNumeric(args[1])) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "Error!" + ChatColor.RED
                                + " The enchantment level you entered is not a number!");
                        return true;
                    }

                    if (customEnchantManager.itemContainsEnchant(item, customEnchant)) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "Error!" + ChatColor.RED
                                + " This item already contains this enchantment!");
                        return true;
                    }

                    if (!customEnchantUtils.isCompatibleWith(customEnchant, item.getType())) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "Error!" + ChatColor.RED
                                + " You can not enchant this enchant on this item!");
                        return true;
                    }

                    if (Integer.parseInt(args[1]) > 3 || Integer.parseInt(args[1]) < 1) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "Error!" + ChatColor.RED
                                + " The enchant level can only be 1, 2, or 3!");
                        return true;
                    }

                    int level = Integer.parseInt(args[1]);

                    if (player.getWorld().getName().equals("ThePit_0")) {
                        int numberOfEnchants = customEnchantManager.getItemEnchants(item).size();

                        if (numberOfEnchants >= 3) {
                            player.sendMessage(ChatColor.DARK_PURPLE + "Error!" + ChatColor.RED
                                    + " You can only put a maximum of 3 enchants in this world!");
                            return true;
                        }

                        int tokens = customEnchantManager.getTokensOnItem(item) + level;

                        if (tokens > 8) {
                            player.sendMessage(ChatColor.DARK_PURPLE + "Error!" + ChatColor.RED
                                    + " You can only have a maximum of 8 tokens in this world!");
                            return true;
                        }

                        int rareTokens = 0;
                        int rareEnchantCount = 0;

                        for (Map.Entry<CustomEnchant, Integer> entry : customEnchantManager.getItemEnchants(item)
                                .entrySet()) {
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
                            player.sendMessage(ChatColor.DARK_PURPLE + "Error!" + ChatColor.RED
                                    + " You can only have 2 rare enchants on an item in this world!");
                            return true;
                        }

                        if (rareTokens > 4) {
                            player.sendMessage(ChatColor.DARK_PURPLE + "Error!" + ChatColor.RED
                                    + " You can only have a maximum of 4 tokens for rare enchants in this world!");
                            return true;
                        }
                    }

                    customEnchantManager.addEnchants(item, level, customEnchant);
                    player.sendMessage(ChatColor.DARK_PURPLE + "Success!" + ChatColor.RED
                            + " You applied the enchantment successfully!");
                    player.updateInventory();
                }
            }
        }

        return true;
    }
}
