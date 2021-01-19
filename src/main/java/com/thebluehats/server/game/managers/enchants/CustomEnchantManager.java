package com.thebluehats.server.game.managers.enchants;

import com.google.common.collect.ImmutableMap;
import com.thebluehats.server.game.utils.PantsData;
import com.thebluehats.server.game.utils.PantsData.FreshPantsColor;
import com.thebluehats.server.game.utils.PantsData.PantsDataValue;
import com.thebluehats.server.game.utils.Registerer;
import com.thebluehats.server.game.utils.RomanNumeralConverter;
import com.thebluehats.server.game.utils.SortCustomEnchantByName;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomEnchantManager implements Registerer<CustomEnchant> {
    private final ArrayList<CustomEnchant> enchants = new ArrayList<>();
    private final SortCustomEnchantByName sortCustomEnchantByName = new SortCustomEnchantByName();

    private final ImmutableMap<Integer, ChatColor> tierColors = ImmutableMap.<Integer, ChatColor>builder()
            .put(1, ChatColor.GREEN).put(2, ChatColor.YELLOW).put(3, ChatColor.RED).build();

    private final JavaPlugin plugin;
    private final RomanNumeralConverter romanNumeralConverter;
    private final PantsData pantsData;
    private final CustomEnchantUtils customEnchantUtils;

    @Inject
    public CustomEnchantManager(JavaPlugin plugin, RomanNumeralConverter romanNumeralConverter,
                                PantsData pantsData, CustomEnchantUtils customEnchantUtils) {
        this.plugin = plugin;
        this.romanNumeralConverter = romanNumeralConverter;
        this.pantsData = pantsData;
        this.customEnchantUtils = customEnchantUtils;
    }

    public ArrayList<CustomEnchant> getEnchants() {
        return enchants;
    }

    @Override
    public void register(CustomEnchant[] enchants) {
        for (CustomEnchant enchant : enchants) {
            if (enchant instanceof Listener) {
                plugin.getServer().getPluginManager().registerEvents((Listener) enchant, plugin);
            }

            this.enchants.add(enchant);
        }

        this.enchants.sort(sortCustomEnchantByName);
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
                properlyCasedKey = key.charAt(0) + key.substring(1).toLowerCase();

                break;
            }
        }

        String textColor = null;

        boolean isFreshItem = isFreshItem(item);

        if (itemKey == null || itemMeta == null) return;

        if (itemKey.equals("LEGGINGS")) {
            ImmutableMap<FreshPantsColor, PantsDataValue> pantsData = this.pantsData.getData();

            if (isFreshItem) {
                FreshPantsColor freshPantsColor = FreshPantsColor
                        .valueOf(ChatColor.stripColor(itemMeta.getDisplayName().split(" ")[1]).toUpperCase());

                textColor = pantsData.get(freshPantsColor).getTextColor().toString();
            } else {
                textColor = itemMeta.getDisplayName().substring(0, 2);
            }
        }

        if (isFreshItem) {
            if (itemKey.equals("LEGGINGS")) {
                itemMeta.setDisplayName(textColor + "Tier I Pants");

                itemMeta.setLore(finalizePantsLore(enchantName, isRareEnchant, level, description, textColor));
            } else {
                itemMeta.setDisplayName(tierColors.get(1) + "Tier I " + properlyCasedKey);
                itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 2, false);

                itemMeta.setLore(finalizeHandheldLore(enchantName, isRareEnchant, level, description));
            }

            item.setItemMeta(itemMeta);

            return;
        }

        if (itemKey.equals("LEGGINGS")) {
            if (tierUp)
                itemMeta.setDisplayName(upgradeTier(itemMeta.getDisplayName(), textColor));

            List<String> lore = itemMeta.getLore();

            trimPantsLoreEnding(lore);

            lore.add("");
            lore.add(formatEnchantName(enchantName, isRareEnchant, level));
            lore.addAll(description);
            lore.add("");
            lore.add(textColor + "As strong as iron");

            itemMeta.setLore(lore);

        } else {
            if (tierUp) itemMeta.setDisplayName(upgradeTier(itemMeta.getDisplayName()));

            List<String> lore = itemMeta.getLore();

            if (lore == null) return;

            lore.add("");
            lore.add(formatEnchantName(enchantName, isRareEnchant, level));
            lore.addAll(description);

            itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 2, false);

            itemMeta.setLore(lore);
        }

        item.setItemMeta(itemMeta);
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

        return finalLore;
    }

    private String formatEnchantName(String name, boolean isRare, int level) {
        return (isRare ? ChatColor.LIGHT_PURPLE + "RARE! " : "") + ChatColor.BLUE + name
                + (level != 1 ? " " + romanNumeralConverter.convertToRomanNumeral(level) : "");
    }

    public boolean isFreshItem(ItemStack item) {
        Pattern p = Pattern.compile("(Mystic Bow|Mystic Sword|Fresh)");
        Matcher m = p.matcher(item.getItemMeta().getDisplayName());

        return m.find();
    }

    private void trimPantsLoreEnding(List<String> lore) {
        lore.remove(lore.size() - 1);
        lore.remove(lore.size() - 1);
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
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = itemMeta.getLore();

        if (lore == null)
            return;

        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);

            if (line.contains(enchant.getName())) {
                while (!lore.get(i).equals("")) {
                    if (lore.size() == i + 1) {
                        lore.remove(i);

                        break;
                    }

                    lore.remove(i);
                }

                lore.remove(i - 1);

                break;
            }
        }

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);
    }

    public HashMap<CustomEnchant, Integer> getItemEnchants(ItemStack item) {
        HashMap<CustomEnchant, Integer> enchantsToLevels = new HashMap<>();

        List<String> lore = item.getItemMeta().getLore();

        if (lore == null)
            return enchantsToLevels;

        for (CustomEnchant enchant : enchants) {
            if (customEnchantUtils.itemHasEnchant(enchant, item)) {
                enchantsToLevels.put(enchant, customEnchantUtils.getEnchantLevel(enchant, item));
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
            if (customEnchantUtils.itemHasEnchant(enchant, item)) {
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
}
