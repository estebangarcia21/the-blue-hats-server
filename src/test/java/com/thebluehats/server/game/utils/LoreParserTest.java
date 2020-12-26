package com.thebluehats.server.game.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.junit.Test;

public class LoreParserTest {
    @Test
    public void BreaksLinesCorrectly() {
        LoreParser loreParser = new LoreParser("A<br/>B");

        ArrayList<String> expectedLore = new ArrayList<>();
        expectedLore.add(ChatColor.GRAY + "A");
        expectedLore.add(ChatColor.GRAY + "B");

        ArrayList<String> parsedLore = loreParser.parse();

        assertEquals(expectedLore, parsedLore);
    }

    @Test
    public void ReplacesTagsWithProperChatColor() {
        LoreParser loreParser = new LoreParser("A <red>B</red> C");

        ArrayList<String> expectedLore = new ArrayList<>();
        expectedLore.add(ChatColor.GRAY + "A " + ChatColor.RED + "B" + ChatColor.GRAY + " C");

        ArrayList<String> parsedLore = loreParser.parse();

        assertEquals(expectedLore, parsedLore);
    }

    @Test
    public void InsertsVariablesAtProperPlace() {
        LoreParser loreParser = new LoreParser("A {0} {1} {0} B");

        String[] variables = new String[2];
        variables[0] = "first";
        variables[1] = "second";

        loreParser.setVariables(variables);

        ArrayList<String> expectedLore = new ArrayList<>();
        expectedLore.add(ChatColor.GRAY + "A first second first B");

        ArrayList<String> parsedLore = loreParser.parse();

        assertEquals(expectedLore, parsedLore);
    }
}
