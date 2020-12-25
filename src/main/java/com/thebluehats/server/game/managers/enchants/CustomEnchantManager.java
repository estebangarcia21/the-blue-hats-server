package com.thebluehats.server.game.managers.enchants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;

import com.google.common.collect.ImmutableMap;
import com.thebluehats.server.game.utils.PantsDataContainer;
import com.thebluehats.server.game.utils.RomanNumeralConverter;
import com.thebluehats.server.game.utils.SortCustomEnchantByName;
import com.thebluehats.server.game.utils.PantsDataContainer.FreshPantsColor;
import com.thebluehats.server.game.utils.PantsDataContainer.PantsData;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomEnchantManager {
    private final ArrayList<CustomEnchant> enchants = new ArrayList<>();
    private final SortCustomEnchantByName sortCustomEnchantByName = new SortCustomEnchantByName();

    private final ImmutableMap<Integer, ChatColor> tierColors = ImmutableMap.<Integer, ChatColor>builder()
            .put(1, ChatColor.GREEN).put(2, ChatColor.YELLOW).put(3, ChatColor.RED).build();

    private final JavaPlugin plugin;
    private final RomanNumeralConverter romanNumeralConverter;
    private final PantsDataContainer pantsDataContainer;

    @Inject
    public CustomEnchantManager(JavaPlugin plugin, RomanNumeralConverter romanNumeralConverter,
            PantsDataContainer pantsDataContainer) {
        this.plugin = plugin;
        this.romanNumeralConverter = romanNumeralConverter;
        this.pantsDataContainer = pantsDataContainer;
    }

    public ArrayList<CustomEnchant> getEnchants() {
        return enchants;
    }

    public void registerEnchant(CustomEnchant enchant) {
        if (enchant instanceof Listener) {
            plugin.getServer().getPluginManager().registerEvents((Listener) enchant, plugin);
        }

        enchants.add(enchant);
        enchants.sort(sortCustomEnchantByName);
    }

    public void addEnchant(ItemStack item, int level, boolean tierUp, CustomEnchant enchant) {
        ArrayList<String> description = enchant.getDescription(level);

        ItemMeta itemMeta = item.getItemMeta();

        String itemTypeName = item.getType().toString();
        String[] keys = new String[] { "LEGGINGS", "SWORD", "BOW" };

        String itemKey = null;
        String properlyCasedKey = null;

        String enchantName = enchant.getName();
        boolean isRareEnchant = enchant.isRareEnchant();

        for (String key : keys) {
            if (itemTypeName.contains(key)) {
                itemKey = key;
                properlyCasedKey = key.substring(0, 1) + key.substring(1).toLowerCase();

                break;
            }
        }

        String textColor = null;

        boolean isFreshItem = isFreshItem(item);

        if (itemKey == "LEGGINGS") {
            ImmutableMap<FreshPantsColor, PantsData> pantsData = pantsDataContainer.getData();

            if (isFreshItem) {
                FreshPantsColor freshPantsColor = FreshPantsColor
                        .valueOf(ChatColor.stripColor(itemMeta.getDisplayName().split(" ")[1]).toUpperCase());

                textColor = pantsData.get(freshPantsColor).getTextColor().toString();
            } else {
                textColor = itemMeta.getDisplayName().substring(0, 2);
            }
        }

        if (isFreshItem) {
            if (itemKey == "LEGGINGS") {
                itemMeta.setDisplayName(textColor + "Tier I Pants");

                itemMeta.setLore(finalizePantsLore(enchantName, isRareEnchant, level, description, textColor));

                item.setItemMeta(itemMeta);

            } else {
                itemMeta.setDisplayName(tierColors.get(1) + "Tier I " + properlyCasedKey);

                itemMeta.setLore(finalizeHandheldLore(enchantName, isRareEnchant, level, description));

                item.setItemMeta(itemMeta);
            }

            return;
        }

        if (itemKey == "LEGGINGS") {
            if (tierUp)
                itemMeta.setDisplayName(upgradeTier(itemMeta.getDisplayName(), textColor));

            List<String> lore = itemMeta.getLore();

            lore = trimPantsLoreEnding(lore);

            lore.add("");
            lore.add(formatEnchantName(enchantName, isRareEnchant, level));
            lore.addAll(description);
            lore.add("");
            lore.add(textColor + "As strong as iron");

            itemMeta.setLore(lore);

            item.setItemMeta(itemMeta);
        } else {
            if (tierUp)
                itemMeta.setDisplayName(upgradeTier(itemMeta.getDisplayName()));

            List<String> lore = itemMeta.getLore();

            lore.add(formatEnchantName(enchantName, isRareEnchant, level));
            lore.addAll(description);
            lore.add("");

            itemMeta.setLore(lore);

            item.setItemMeta(itemMeta);
        }
    }

    private String upgradeTier(String displayName) {
        String[] displayNameTokens = displayName.split(" ");

        int tier = romanNumeralConverter.convertRomanNumeralToInteger(ChatColor.stripColor(displayNameTokens[1]));
        String itemName = ChatColor.stripColor(displayNameTokens[2]);

        int nextTier = tier + 1;

        return tierColors.get(nextTier) + "Tier " + romanNumeralConverter.convertToRomanNumeral(nextTier) + " "
                + itemName;
    }

    private String upgradeTier(String displayName, String textColor) {
        String[] displayNameTokens = displayName.split(" ");

        int tier = romanNumeralConverter.convertRomanNumeralToInteger(ChatColor.stripColor(displayNameTokens[1]));
        String itemName = ChatColor.stripColor(displayNameTokens[2]);

        return textColor + "Tier " + romanNumeralConverter.convertToRomanNumeral(tier + 1) + " " + itemName;
    }

    private ArrayList<String> finalizePantsLore(String name, boolean isRare, int level, ArrayList<String> description,
            String textColor) {
        ArrayList<String> finalLore = new ArrayList<>();

        finalLore.add(ChatColor.GRAY + "Lives: " + ChatColor.GREEN + "69" + ChatColor.GRAY + "/420");
        finalLore.add("");
        finalLore.add(formatEnchantName(name, isRare, level));
        finalLore.addAll(description);
        finalLore.add("");
        finalLore.add(textColor + "As strong as iron");

        return finalLore;
    }

    private ArrayList<String> finalizeHandheldLore(String name, boolean isRare, int level,
            ArrayList<String> description) {
        ArrayList<String> finalLore = new ArrayList<>();

        finalLore.add(ChatColor.GRAY + "Lives: " + ChatColor.GREEN + "69" + ChatColor.GRAY + "/420");
        finalLore.add("");
        finalLore.add(formatEnchantName(name, isRare, level));
        finalLore.addAll(description);
        finalLore.add("");

        return finalLore;
    }

    private String formatEnchantName(String name, boolean isRare, int level) {
        return (isRare ? ChatColor.LIGHT_PURPLE + "Rare! " : "") + ChatColor.BLUE + name
                + (level != 1 ? " " + romanNumeralConverter.convertToRomanNumeral(level) : "");
    }

    public boolean isFreshItem(ItemStack item) {
        return item.getItemMeta().getDisplayName().contains("Fresh");
    }

    private List<String> trimPantsLoreEnding(List<String> lore) {
        List<String> trimmedLore = lore;

        trimmedLore.remove(trimmedLore.size() - 1);
        trimmedLore.remove(trimmedLore.size() - 1);

        return trimmedLore;
    }

    public int getItemLives(ItemStack item) {
        return getItemLivesToken(item, 0);
    }

    public int getMaximumItemLives(ItemStack item) {
        return getItemLivesToken(item, 1);
    }

    private int getItemLivesToken(ItemStack item, int index) {
        if (item.getItemMeta().getLore() == null)
            return 0;

        ArrayList<String> displayNameTokens = new ArrayList<>(
                Arrays.asList(ChatColor.stripColor(item.getItemMeta().getDisplayName()).split(" ")));

        if (displayNameTokens.contains("Fresh") || displayNameTokens.contains("Mystic"))
            return 0;

        List<String> lore = item.getItemMeta().getLore();
        String livesLine = lore.get(0);

        return Integer.parseInt(ChatColor.stripColor(livesLine.split(" ")[1]).split("/")[index]);
    }

    public void setItemLives(ItemStack item, int value) {
        if (item.getItemMeta().getLore() == null)
            return;

        List<String> lore = item.getItemMeta().getLore();
        lore.set(0, ChatColor.GRAY + "Lives: " + (value > 3 ? ChatColor.GREEN : ChatColor.RED) + value + ChatColor.GRAY
                + "/" + getMaximumItemLives(item));

        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);

        item.setItemMeta(meta);
    }

    public void setMaximumItemLives(ItemStack item, int value) {
        if (item.getItemMeta().getLore() == null)
            return;

        int lives = getItemLives(item);

        if (lives > value)
            lives = value;

        List<String> lore = item.getItemMeta().getLore();
        lore.set(0, ChatColor.GRAY + "Lives: " + (value > 3 ? ChatColor.GREEN : ChatColor.RED) + lives + ChatColor.GRAY
                + "/" + value);

        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);

        item.setItemMeta(meta);
    }

    public void removeEnchant(ItemStack item, CustomEnchant enchant) {
        List<String> lore = item.getItemMeta().getLore();

        if (lore == null)
            return;

        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);

            if (line.contains(enchant.getName())) {
                for (int j = i - 1; j < lore.size() - i; j++) {
                    if (lore.get(i) == "") {
                        lore.remove(i);

                        break;
                    }

                    lore.remove(i);
                }

                break;
            }
        }
    }

    public boolean itemContainsEnchant(ItemStack item, CustomEnchant enchant) {
        if (item.getItemMeta().getLore() == null || enchant == null)
            return false;

        List<String> lore = item.getItemMeta().getLore();

        String appendRare = "";

        if (enchant.isRareEnchant())
            appendRare = ChatColor.LIGHT_PURPLE + "RARE! ";

        if (lore.contains(appendRare + ChatColor.BLUE + enchant.getName()))
            return true;

        for (int i = 2; i <= 3; i++) {
            if (lore.contains(appendRare + ChatColor.BLUE + enchant.getName() + " "
                    + romanNumeralConverter.convertToRomanNumeral(i)))
                return true;
        }

        return false;
    }

    public HashMap<CustomEnchant, Integer> getItemEnchants(ItemStack item) {
        HashMap<CustomEnchant, Integer> enchantsToLevels = new HashMap<>();

        if (item.getType() == Material.AIR)
            return enchantsToLevels;
        if (item.getItemMeta().getLore() == null)
            return enchantsToLevels;

        for (String string : item.getItemMeta().getLore()) {
            ArrayList<String> enchantData = new ArrayList<>(Arrays.asList(string.split(" ")));
            StringBuilder enchantName = new StringBuilder();
            int level = 0;

            if (enchantData.size() == 0) {
                continue;
            }

            for (int i = 0; i < enchantData.size(); i++) {
                enchantData.set(i, ChatColor.stripColor(enchantData.get(i)));
            }

            if (enchantData.get(0).equalsIgnoreCase("RARE!")) {
                enchantData.remove(0);
            }

            for (int i = 0; i < enchantData.size(); i++) {
                if (romanNumeralConverter.convertRomanNumeralToInteger(enchantData.get(i)) == -1) {
                    enchantName.append(enchantData.get(i));
                    if (i != enchantData.size() - 1)
                        enchantName.append(" ");
                } else {
                    level = romanNumeralConverter.convertRomanNumeralToInteger(enchantData.get(i));
                }
            }

            String name = enchantName.toString().trim();

            for (CustomEnchant enchant : getEnchants()) {
                if (enchant.getName().equals(name)) {
                    enchantsToLevels.put(enchant, level);
                    break;
                }
            }
        }

        return enchantsToLevels;
    }

    public int getTokensOnItem(ItemStack item) {
        if (item.getType() == Material.AIR || item.getItemMeta().getLore() == null)
            return 0;

        int tokens = 0;

        for (Map.Entry<CustomEnchant, Integer> entry : getItemEnchants(item).entrySet()) {
            tokens += entry.getValue();
        }

        return tokens;
    }

    public List<CustomEnchant> getRawItemEnchants(ItemStack item) {
        if (item == null)
            return new ArrayList<>();

        ArrayList<CustomEnchant> enchants = new ArrayList<>();

        for (CustomEnchant enchant : getEnchants()) {
            if (itemContainsEnchant(item, enchant)) {
                enchants.add(enchant);
            }
        }

        return enchants;
    }

    public int getItemTier(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        ArrayList<String> tokens = new ArrayList<>(
                Arrays.asList(ChatColor.stripColor(meta.getDisplayName()).split(" ")));

        if (tokens.contains("I")) {
            return 1;
        } else if (tokens.contains("II")) {
            return 2;
        } else if (tokens.contains("III")) {
            return 3;
        } else if (tokens.contains("Fresh") || tokens.contains("Mystic")) {
            return 0;
        }

        return -1;
    }

    public boolean percentChance(double percent) {
        return Double.parseDouble(
                new DecimalFormat("#0.0").format(ThreadLocalRandom.current().nextDouble(0, 99))) <= percent;
    }
}
