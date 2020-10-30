package me.stevemmmmm.server.game.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.stevemmmmm.server.game.enchants.CustomEnchantManager;

public class TutorialCommand implements CommandExecutor {
    private CustomEnchantManager manager;

    public TutorialCommand(CustomEnchantManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (label.equalsIgnoreCase("tutorial")) {
                int pages = 1;
                int page = 1;

                if (args.length == 1) {
                    if (!StringUtils.isNumeric(args[0])) {
                        player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Please specify a correct page number!");
                        return true;
                    }

                    page = Integer.parseInt(args[0]);
                }

                if (page <= 0 || page > pages + 1) {
                    player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Please specify a correct page number!");
                    return true;
                }

                player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "TUTORIAL! (" + page + "/" + (pages + 1) + ")");

                for (int i = 0; i < 9; i++) {
                    int index = i + ((page - 1) * 9);

                    if (index > manager.getEnchants().size() - 1) {
                        player.sendMessage(" ");
                        continue;
                    }

                    player.sendMessage(ChatColor.GRAY + "■ " + ChatColor.RED
                            + manager.getEnchants().get(index).getName() + ChatColor.GOLD
                            + " ▶ " + ChatColor.YELLOW
                            + manager.getEnchants().get(index).getEnchantReferenceName());
                }
            }
        }

        return true;
    }
}
