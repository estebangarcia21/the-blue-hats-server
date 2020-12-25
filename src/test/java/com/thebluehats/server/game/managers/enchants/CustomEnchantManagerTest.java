package com.thebluehats.server.game.managers.enchants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import com.thebluehats.server.game.utils.PantsDataContainer;
import com.thebluehats.server.game.utils.RomanNumeralConverter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.Test;

public class CustomEnchantManagerTest {
    @Test
    public void RemovesAnEnchantCorrectly() {
        CustomEnchantManager customEnchantManager = new CustomEnchantManager(mock(JavaPlugin.class),
                mock(RomanNumeralConverter.class), mock(PantsDataContainer.class));

        ItemStack item = mock(ItemStack.class);
        ItemMeta itemMeta = mock(ItemMeta.class);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Lives: " + ChatColor.GREEN + "69" + ChatColor.GRAY + "/420");
        lore.add("");
        lore.add(ChatColor.BLUE + "Wasp II");
        lore.add("Does something amazing");
        lore.add("and adds weakness");
        lore.add("");
        lore.add(ChatColor.BLUE + "Something III");
        lore.add("Does something amazing as well");
        lore.add("and adds weakness and speed");
        lore.add("");
        lore.add("As strong as iron");

        CustomEnchant customEnchant = mock(CustomEnchant.class);
        when(customEnchant.getName()).thenReturn("Wasp");

        when(item.getItemMeta()).thenReturn(itemMeta);
        when(itemMeta.getLore()).thenReturn(lore);

        customEnchantManager.removeEnchant(item, customEnchant);

        ArrayList<String> expectedLore = new ArrayList<>();
        expectedLore.add(ChatColor.GRAY + "Lives: " + ChatColor.GREEN + "69" + ChatColor.GRAY + "/420");
        expectedLore.add("");
        expectedLore.add(ChatColor.BLUE + "Something III");
        expectedLore.add("Does something amazing as well");
        expectedLore.add("and adds weakness and speed");
        expectedLore.add("");
        expectedLore.add("As strong as iron");

        assertEquals(expectedLore, lore);
    }

    @Test
    public void AddsAnEnchantToFreshHandheldItem() {
        CustomEnchantManager customEnchantManager = new CustomEnchantManager(mock(JavaPlugin.class),
                mock(RomanNumeralConverter.class), mock(PantsDataContainer.class));

        ItemStack item = mock(ItemStack.class);
        ItemMeta itemMeta = mock(ItemMeta.class);

        ArrayList<String> freshItemLore = new ArrayList<>();
        freshItemLore.add(ChatColor.GRAY + "Kept on death");
        freshItemLore.add(ChatColor.GRAY + "Used in the mystic well");

        CustomEnchant customEnchant = mock(CustomEnchant.class);
        ArrayList<String> customEnchantLore = new ArrayList<String>();
        customEnchantLore.add(ChatColor.GRAY + "A cool double");
        customEnchantLore.add(ChatColor.GRAY + "line enchant!");

        when(customEnchant.getName()).thenReturn("Image");
        when(customEnchant.isRareEnchant()).thenReturn(true);
        when(customEnchant.getDescription(1)).thenReturn(customEnchantLore);

        when(item.getItemMeta()).thenReturn(itemMeta);
        when(item.getType()).thenReturn(Material.BOW);
        when(itemMeta.getLore()).thenReturn(freshItemLore);
        when(itemMeta.getDisplayName()).thenReturn(ChatColor.AQUA + "Fresh Mystic Bow");

        customEnchantManager.addEnchant(item, 1, false, customEnchant);

        ArrayList<String> expectedLore = new ArrayList<>();
        expectedLore.add(ChatColor.GRAY + "Lives: " + ChatColor.GREEN + "69" + ChatColor.GRAY + "/420");
        expectedLore.add("");
        expectedLore.add(ChatColor.LIGHT_PURPLE + "Rare! " + ChatColor.BLUE + "Image");
        expectedLore.add(ChatColor.GRAY + "A cool double");
        expectedLore.add(ChatColor.GRAY + "line enchant!");
        expectedLore.add("");

        verify(itemMeta).setDisplayName(ChatColor.GREEN + "Tier I Bow");
        verify(itemMeta).setLore(expectedLore);
    }

    @Test
    public void AddsAnEnchantToFreshPants() {
        CustomEnchantManager customEnchantManager = new CustomEnchantManager(mock(JavaPlugin.class),
                mock(RomanNumeralConverter.class), new PantsDataContainer());

        ItemStack item = mock(ItemStack.class);
        ItemMeta itemMeta = mock(ItemMeta.class);

        ArrayList<String> freshItemLore = new ArrayList<>();
        freshItemLore.add(ChatColor.GRAY + "Kept on death");
        freshItemLore.add(ChatColor.GRAY + "Used in the mystic well");

        CustomEnchant customEnchant = mock(CustomEnchant.class);
        ArrayList<String> customEnchantLore = new ArrayList<String>();
        customEnchantLore.add(ChatColor.GRAY + "A cool double");
        customEnchantLore.add(ChatColor.GRAY + "line enchant!");

        when(customEnchant.getName()).thenReturn("Image");
        when(customEnchant.isRareEnchant()).thenReturn(true);
        when(customEnchant.getDescription(1)).thenReturn(customEnchantLore);

        when(item.getItemMeta()).thenReturn(itemMeta);
        when(item.getType()).thenReturn(Material.LEATHER_LEGGINGS);
        when(itemMeta.getLore()).thenReturn(freshItemLore);
        when(itemMeta.getDisplayName()).thenReturn(ChatColor.BLUE + "Fresh Blue Pants");

        customEnchantManager.addEnchant(item, 1, false, customEnchant);

        ArrayList<String> expectedLore = new ArrayList<>();
        expectedLore.add(ChatColor.GRAY + "Lives: " + ChatColor.GREEN + "69" + ChatColor.GRAY + "/420");
        expectedLore.add("");
        expectedLore.add(ChatColor.LIGHT_PURPLE + "Rare! " + ChatColor.BLUE + "Image");
        expectedLore.add(ChatColor.GRAY + "A cool double");
        expectedLore.add(ChatColor.GRAY + "line enchant!");
        expectedLore.add("");
        expectedLore.add(ChatColor.BLUE + "As strong as iron");

        verify(itemMeta).setDisplayName(ChatColor.BLUE + "Tier I Pants");
        verify(itemMeta).setLore(expectedLore);
    }

    @Test
    public void AddsAnEnchantToPants() {
        CustomEnchantManager customEnchantManager = new CustomEnchantManager(mock(JavaPlugin.class),
                new RomanNumeralConverter(), new PantsDataContainer());

        ItemStack item = mock(ItemStack.class);
        ItemMeta itemMeta = mock(ItemMeta.class);

        ArrayList<String> itemLore = new ArrayList<String>();
        itemLore.add(ChatColor.GRAY + "Lives: " + ChatColor.GREEN + "69" + ChatColor.GRAY + "/420");
        itemLore.add("");
        itemLore.add(ChatColor.LIGHT_PURPLE + "Rare! " + ChatColor.BLUE + "Image");
        itemLore.add(ChatColor.GRAY + "A cool double");
        itemLore.add(ChatColor.GRAY + "line enchant!");
        itemLore.add("");
        itemLore.add(ChatColor.BLUE + "As strong as iron");

        CustomEnchant customEnchant = mock(CustomEnchant.class);

        ArrayList<String> customEnchantDescription = new ArrayList<>();
        customEnchantDescription.add(ChatColor.GRAY + "A cool double");
        customEnchantDescription.add(ChatColor.GRAY + "line enchant!");

        when(customEnchant.getName()).thenReturn("Image");
        when(customEnchant.isRareEnchant()).thenReturn(true);
        when(customEnchant.getDescription(2)).thenReturn(customEnchantDescription);

        when(item.getItemMeta()).thenReturn(itemMeta);
        when(item.getType()).thenReturn(Material.LEATHER_LEGGINGS);
        when(itemMeta.getLore()).thenReturn(itemLore);
        when(itemMeta.getDisplayName()).thenReturn(ChatColor.BLUE + "Tier I Pants");

        customEnchantManager.addEnchant(item, 2, true, customEnchant);

        ArrayList<String> expectedLore = new ArrayList<>();
        expectedLore.add(ChatColor.GRAY + "Lives: " + ChatColor.GREEN + "69" + ChatColor.GRAY + "/420");
        expectedLore.add("");
        expectedLore.add(ChatColor.LIGHT_PURPLE + "Rare! " + ChatColor.BLUE + "Image");
        expectedLore.add(ChatColor.GRAY + "A cool double");
        expectedLore.add(ChatColor.GRAY + "line enchant!");
        expectedLore.add("");
        expectedLore.add(ChatColor.LIGHT_PURPLE + "Rare! " + ChatColor.BLUE + "Image II");
        expectedLore.add(ChatColor.GRAY + "A cool double");
        expectedLore.add(ChatColor.GRAY + "line enchant!");
        expectedLore.add("");
        expectedLore.add(ChatColor.BLUE + "As strong as iron");

        verify(itemMeta).setDisplayName(ChatColor.BLUE + "Tier II Pants");
        verify(itemMeta).setLore(expectedLore);
    }

    @Test
    public void AddsAnEnchantToAHandheldItem() {
        CustomEnchantManager customEnchantManager = new CustomEnchantManager(mock(JavaPlugin.class),
                new RomanNumeralConverter(), new PantsDataContainer());

        ItemStack item = mock(ItemStack.class);
        ItemMeta itemMeta = mock(ItemMeta.class);

        ArrayList<String> itemLore = new ArrayList<String>();
        itemLore.add(ChatColor.GRAY + "Lives: " + ChatColor.GREEN + "69" + ChatColor.GRAY + "/420");
        itemLore.add("");
        itemLore.add(ChatColor.LIGHT_PURPLE + "Rare! " + ChatColor.BLUE + "Image");
        itemLore.add(ChatColor.GRAY + "A cool double");
        itemLore.add(ChatColor.GRAY + "line enchant!");
        itemLore.add("");

        CustomEnchant customEnchant = mock(CustomEnchant.class);

        ArrayList<String> customEnchantDescription = new ArrayList<>();
        customEnchantDescription.add(ChatColor.GRAY + "A cool double");
        customEnchantDescription.add(ChatColor.GRAY + "line enchant!");

        when(customEnchant.getName()).thenReturn("Image");
        when(customEnchant.isRareEnchant()).thenReturn(true);
        when(customEnchant.getDescription(2)).thenReturn(customEnchantDescription);

        when(item.getItemMeta()).thenReturn(itemMeta);
        when(item.getType()).thenReturn(Material.BOW);
        when(itemMeta.getLore()).thenReturn(itemLore);
        when(itemMeta.getDisplayName()).thenReturn(ChatColor.GREEN + "Tier I Bow");

        customEnchantManager.addEnchant(item, 2, true, customEnchant);

        ArrayList<String> expectedLore = new ArrayList<>();
        expectedLore.add(ChatColor.GRAY + "Lives: " + ChatColor.GREEN + "69" + ChatColor.GRAY + "/420");
        expectedLore.add("");
        expectedLore.add(ChatColor.LIGHT_PURPLE + "Rare! " + ChatColor.BLUE + "Image");
        expectedLore.add(ChatColor.GRAY + "A cool double");
        expectedLore.add(ChatColor.GRAY + "line enchant!");
        expectedLore.add("");
        expectedLore.add(ChatColor.LIGHT_PURPLE + "Rare! " + ChatColor.BLUE + "Image II");
        expectedLore.add(ChatColor.GRAY + "A cool double");
        expectedLore.add(ChatColor.GRAY + "line enchant!");
        expectedLore.add("");

        verify(itemMeta).setDisplayName(ChatColor.YELLOW + "Tier II Bow");
        verify(itemMeta).setLore(expectedLore);
    }

    @Test
    public void ProperlyChecksIfAnItemIsFresh() {
        CustomEnchantManager customEnchantManager = new CustomEnchantManager(mock(JavaPlugin.class),
                mock(RomanNumeralConverter.class), mock(PantsDataContainer.class));

        ItemStack item = mock(ItemStack.class);
        ItemMeta itemMeta = mock(ItemMeta.class);

        when(item.getItemMeta()).thenReturn(itemMeta);
        when(itemMeta.getDisplayName()).thenReturn(ChatColor.BLUE + "Tier I Pants");

        assertFalse(customEnchantManager.isFreshItem(item));
    }
}
