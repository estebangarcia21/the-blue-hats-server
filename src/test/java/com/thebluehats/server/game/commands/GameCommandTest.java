package com.thebluehats.server.game.commands;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.Test;

public class GameCommandTest {
    @Test
    public void SendsTheProperUsageMessage() {
        GameCommand gameCommand = new GameCommand() {
            @Override
            public boolean onCommand(CommandSender commandSender, Command cmd, String label, String[] args) {
                return true;
            }
        };

        Player player = mock(Player.class);

        gameCommand.sendUsageMessage(player, "give", "gives an item", "player", "item", "amount");

        String expectedMessage = ChatColor.DARK_PURPLE + "/give - " + ChatColor.RED + "gives an item"
                + ChatColor.DARK_PURPLE + " | Usage" + ChatColor.DARK_PURPLE + "/give " + ChatColor.RED
                + "<player> <item> <amount>";

        verify(player).sendMessage(expectedMessage);
    }

    @Test
    public void SendsTheProperErrorMessage() {
        GameCommand gameCommand = new GameCommand() {
            @Override
            public boolean onCommand(CommandSender commandSender, Command cmd, String label, String[] args) {
                return true;
            }
        };

        Player player = mock(Player.class);

        gameCommand.sendErrorMessage(player, "Something happend!");

        String expectedMessage = ChatColor.DARK_PURPLE + "Error! " + ChatColor.RED + "Something happend!";

        verify(player).sendMessage(expectedMessage);
    }
}
