package com.thebluehats.server.game.commands;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MysticEnchantsCommand extends GameCommand {
    private final CustomEnchantManager customEnchantManager;

    @Inject
    public MysticEnchantsCommand(CustomEnchantManager manager) {
        this.customEnchantManager = manager;
    }

    @Override
    public String[] getCommandNames() {
        return new String[] { "pitenchants" };
    }

    @Override
    public String getUsageMessage(String cmd) {
        return formatStandardUsageMessage(cmd, "Lists the available Custom Enchants and their enchant names.",
                "pagenumber");
    }

    @Override
    public void runCommand(Player player, String commandName, String[] args) {
        int pages = customEnchantManager.getEnchants().size() / 9;
        int page = 1;

        if (args.length == 1) {
            if (!StringUtils.isNumeric(args[0])) {
                player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString()
                        + "Please specify a correct page number!");

                return;
            }

            page = Integer.parseInt(args[0]);
        }

        if (page <= 0 || page > pages + 1) {
            player.sendMessage(
                    ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Please specify a correct page number!");
            return;
        }

        player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Mystic Enchants (" + page + "/"
                + (pages + 1) + ")");

        for (int i = 0; i < 9; i++) {
            int index = i + ((page - 1) * 9);

            if (index > customEnchantManager.getEnchants().size() - 1) {
                player.sendMessage(" ");
                continue;
            }

            player.sendMessage(ChatColor.GRAY + "■ " + ChatColor.RED
                    + customEnchantManager.getEnchants().get(index).getName() + ChatColor.GOLD + " ▶ "
                    + ChatColor.YELLOW + customEnchantManager.getEnchants().get(index).getEnchantReferenceName());
        }
    }
}
