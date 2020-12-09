package com.thebluehats.server.game.utils;

import static org.junit.Assert.assertEquals;

import org.bukkit.ChatColor;
import org.junit.Test;

public class CommandUtilsTest {
    @Test
    public void FormatsCommandCorrectly() {
        assertEquals(CommandUtils.formatMessage("duel", "/duel <player>"),
                String.format("%s%s - Usage: %s%s", ChatColor.DARK_PURPLE, "duel", ChatColor.RED, "/duel <player>"));
    }
}
