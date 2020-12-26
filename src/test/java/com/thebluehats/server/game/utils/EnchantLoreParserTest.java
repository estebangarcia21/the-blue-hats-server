package com.thebluehats.server.game.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.junit.Test;

public class EnchantLoreParserTest {
    @Test
    public void InsertsProperVariablesBasedOnEnchantLevel() {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("A {0} {1} {2} C");

        String[][] variables = new String[3][];
        variables[0] = new String[] { "1", "2", "3" };
        variables[1] = new String[] { "A", "B", "C" };
        variables[2] = new String[] { "D", "E", "F" };

        enchantLoreParser.setVariables(variables);

        ArrayList<String> expectedLore = new ArrayList<>();
        expectedLore.add(ChatColor.GRAY + "A 2 B E C");

        ArrayList<String> parsedLore = enchantLoreParser.parseForLevel(2);

        assertEquals(expectedLore, parsedLore);
    }

    @Test
    public void CorrectlyInsertsSingleVariable() {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("{0}");

        enchantLoreParser.setSingleVariable("A", "B", "C");

        ArrayList<String> expectedLore = new ArrayList<>();
        expectedLore.add(ChatColor.GRAY + "A");

        ArrayList<String> parsedLore = enchantLoreParser.parseForLevel(1);

        assertEquals(expectedLore, parsedLore);
    }

    @Test
    public void AppendsLore() {
        int level = 1;

        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("A B C");
        enchantLoreParser.addTextIf(level == 1, "<br/>A");

        ArrayList<String> expectedLore = new ArrayList<>();
        expectedLore.add(ChatColor.GRAY + "A B C");
        expectedLore.add(ChatColor.GRAY + "A");

        ArrayList<String> parsedLore = enchantLoreParser.parseForLevel(level);

        assertEquals(expectedLore, parsedLore);
    }
}