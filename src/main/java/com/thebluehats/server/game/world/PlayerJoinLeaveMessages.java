package com.thebluehats.server.game.world;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveMessages implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Welcome Back to The Blue Hats Server! " + ChatColor.AQUA
                + event.getPlayer().getName() + ChatColor.GREEN + " just joined the server!");
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "RIP! " + ChatColor.AQUA
                + event.getPlayer().getName() + ChatColor.RED + " just left the server!");
    }
}
