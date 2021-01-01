package com.thebluehats.server.game.managers.combat;

import org.bukkit.ChatColor;

public enum CombatStatus {
    COMBAT(ChatColor.RED + "Fighting"),
    IDLING(ChatColor.GREEN + "Idling");

    private final String formatted;

    CombatStatus(String formatted) {
        this.formatted = formatted;
    }

    public String getFormattedStatus() {
        return formatted;
    }
}
