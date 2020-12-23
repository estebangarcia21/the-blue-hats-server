package com.thebluehats.server.game.commands;

import static org.junit.Assert.assertEquals;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.junit.Test;

public class GameCommandTest {
    @Test
    public void FormatsTheUsageMessageCorrectly() {
        GameCommand gameCommand = new GameCommand() {
            public String[] getCommandNames() {
                return null;
            }

            public String getUsageMessage(String cmd) {
                return "";
            }

            public void runCommand(Player player, String commandName, String[] args) {
            }
        };

        String formattedUsageMessage = gameCommand.formatStandardUsageMessage("give", "gives an item", "player", "item",
                "amount");

        String expectedMessage = ChatColor.DARK_PURPLE + "/give - " + ChatColor.RED + "gives an item"
                + ChatColor.DARK_PURPLE + " | Usage" + ChatColor.DARK_PURPLE + "/give " + ChatColor.RED
                + "<player> <item> <amount>";

        assertEquals(expectedMessage, formattedUsageMessage);
    }

    @Test
    public void FormatsTheErrorMessageCorrectly() {
        GameCommand gameCommand = new GameCommand() {
            public String[] getCommandNames() {
                return null;
            }

            public String getUsageMessage(String cmd) {
                return "";
            }

            public void runCommand(Player player, String commandName, String[] args) {
            }
        };

        String formattedErrorMessage = gameCommand.formatStandardErrorMessage("Something happend!");

        String expectedMessage = ChatColor.DARK_PURPLE + "Error! " + ChatColor.RED + "Something happend!";

        assertEquals(expectedMessage, formattedErrorMessage);
    }
}
