package com.thebluehats.server.game.managers.enchants;

import java.util.List;

import com.google.inject.Inject;
import com.thebluehats.server.game.utils.RomanNumeralConverter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class CustomEnchantUtils {
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
        if (item.getItemMeta().getLore() == null)
            return false;

        String enchantName = enchant.getName();

        List<String> lore = item.getItemMeta().getLore();

        String appendRare = "";
        if (enchant.isRareEnchant())
            appendRare = ChatColor.LIGHT_PURPLE + "RARE! ";

        if (lore.contains(appendRare + ChatColor.BLUE + enchantName))
            return true;

        for (int i = 2; i <= 3; i++) {
            if (lore.contains(
                    appendRare + ChatColor.BLUE + enchantName + " " + romanNumeralConverter.convertToRomanNumeral(i)))
                return true;
        }

        return false;
    }

    public int getEnchantLevel(CustomEnchant enchant, ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return 0;
        if (item.getItemMeta().getLore() == null)
            return 0;

        String enchantName = enchant.getName();

        List<String> lore = item.getItemMeta().getLore();

        String appendRare = "";
        if (enchant.isRareEnchant())
            appendRare = ChatColor.LIGHT_PURPLE + "RARE! ";

        if (lore.contains(appendRare + ChatColor.BLUE + enchantName))
            return 1;

        for (int i = 2; i <= 3; i++) {
            if (lore.contains(
                    appendRare + ChatColor.BLUE + enchantName + " " + romanNumeralConverter.convertToRomanNumeral(i))) {
                return i;
            }
        }

        return 0;
    }
}
