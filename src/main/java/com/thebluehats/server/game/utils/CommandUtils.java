package com.thebluehats.server.game.utils;

import org.bukkit.ChatColor;

public final class CommandUtils {
    private CommandUtils() {
    }

    public static String formatMessage(String primary, String message) {
        return String.format("%s%s - Usage: %s%s", ChatColor.DARK_PURPLE, primary, ChatColor.RED, message);
    }
}
