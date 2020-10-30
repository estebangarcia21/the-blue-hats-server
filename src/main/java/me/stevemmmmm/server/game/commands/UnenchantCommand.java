package me.stevemmmmm.server.game.commands;

import me.stevemmmmm.server.game.enchants.CustomEnchant;
import me.stevemmmmm.server.game.enchants.CustomEnchantManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UnenchantCommand implements CommandExecutor {
    private CustomEnchantManager manager;

    public UnenchantCommand(CustomEnchantManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("unenchant")) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.DARK_PURPLE + "Usage:" + ChatColor.RED + " /unenchant <enchant>");
                } else {
                    CustomEnchant customEnchant = null;

                    for (CustomEnchant enchant : manager.getEnchants()) {
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

                    if (args.length > 1) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "Error!" + ChatColor.RED + " Too many arguments!");
                        return true;
                    }

                    if (item.getType() != Material.LEATHER_LEGGINGS && item.getType() != Material.GOLDEN_SWORD
                            && item.getType() != Material.BOW) {
                        player.sendMessage(
                                ChatColor.DARK_PURPLE + "Error!" + ChatColor.RED + " You can not enchant this item!");
                        return true;
                    }

                    if (!manager.itemContainsEnchant(item, customEnchant)) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "Error!" + ChatColor.RED
                                + " This item does not have the specified enchant!");
                        return true;
                    }

                    manager.removeEnchant(item, customEnchant);
                    player.sendMessage(ChatColor.DARK_PURPLE + "Success!" + ChatColor.RED
                            + " You unenchanted the enchant successfully!");
                    player.updateInventory();
                }
            }
        }

        return true;
    }
}
