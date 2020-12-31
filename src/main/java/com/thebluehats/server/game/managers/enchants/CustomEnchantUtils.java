package com.thebluehats.server.game.managers.enchants;

import java.util.List;

import com.google.inject.Inject;
import com.thebluehats.server.game.utils.RomanNumeralConverter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomEnchantUtils {
    private final RomanNumeralConverter romanNumeralConverter;

    @Inject
    public CustomEnchantUtils(RomanNumeralConverter romanNumeralConverter) {
        this.romanNumeralConverter = romanNumeralConverter;
    }

    public boolean isCompatibleWith(CustomEnchant enchant, Material material) {
        for (Material mat : enchant.getEnchantItemTypes()) {
            if (mat == material) {
                return true;
            }
        }

        return false;
    }

    public boolean itemHasEnchant(CustomEnchant enchant, ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return false;

        List<String> lore = item.getItemMeta().getLore();

        if (lore == null)
            return false;

        for (String line : lore) {
            if (line.contains(enchant.getName())) {
                return true;
            }
        }

        return false;
    }

    public int getEnchantLevel(CustomEnchant enchant, ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return 0;

        List<String> lore = item.getItemMeta().getLore();

        if (lore == null)
            return 0;

        for (String line : lore) {
            if (line.contains(enchant.getName())) {
                String strippedLine = ChatColor.stripColor(line);

                String[] lineTokens = strippedLine.split(" ");
                String numeral = lineTokens[lineTokens.length - 1];

                return numeral == null ? 1 : romanNumeralConverter.convertRomanNumeralToInteger(numeral);
            }
        }

        return 0;
    }
}
