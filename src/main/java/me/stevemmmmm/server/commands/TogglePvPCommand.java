package me.stevemmmmm.server.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

import me.stevemmmmm.server.game.managers.DamageManager;

public class TogglePvPCommand implements CommandExecutor, Listener {
    public static boolean pvpIsToggledOff;

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (pvpIsToggledOff) {
            DamageManager.getInstance().setEventAsCanceled(event);
        }
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (pvpIsToggledOff) {
            event.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (s.equalsIgnoreCase("togglepvp")) {
                if (player.isOp()) {
                    pvpIsToggledOff = !pvpIsToggledOff;
                    player.sendMessage(ChatColor.RED + "PvP is turned " + (pvpIsToggledOff ? "off!" : "on!"));
                }
            }
        }

        return true;
    }
}
