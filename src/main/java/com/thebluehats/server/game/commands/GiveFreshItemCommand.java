package com.thebluehats.server.game.commands;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;
import com.thebluehats.server.game.utils.LoreParser;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class GiveFreshItemCommand extends GameCommand {
    private final ImmutableMap<FreshPantsColor, FreshPantsData> pantsData = ImmutableMap
            .<FreshPantsColor, FreshPantsData>builder()
            .put(FreshPantsColor.RED, new FreshPantsData(0xFF5555, ChatColor.RED))
            .put(FreshPantsColor.GREEN, new FreshPantsData(0x55FF55, ChatColor.GREEN))
            .put(FreshPantsColor.BLUE, new FreshPantsData(0x55555FF, ChatColor.BLUE))
            .put(FreshPantsColor.YELLOW, new FreshPantsData(0xFFFF55, ChatColor.YELLOW))
            .put(FreshPantsColor.ORANGE, new FreshPantsData(0xFFAA00, ChatColor.GOLD))
            .put(FreshPantsColor.DARK, new FreshPantsData(0x000000, ChatColor.DARK_PURPLE))
            .put(FreshPantsColor.SEWER, new FreshPantsData(0x7DC383, ChatColor.DARK_AQUA))
            .put(FreshPantsColor.AQUA, new FreshPantsData(0x55FFFF, ChatColor.DARK_AQUA)).build();

    private final ArrayList<String> freshPantsColors = new ArrayList<>();
    private final ArrayList<String> handheldFreshItems = new ArrayList<>();

    public GiveFreshItemCommand() {
        for (FreshPantsColor freshPantsColor : FreshPantsColor.values()) {
            freshPantsColors.add(freshPantsColor.toString());
        }

        for (HandheldFreshItem handheldFreshItem : HandheldFreshItem.values()) {
            handheldFreshItems.add(handheldFreshItem.toString());
        }
    }

    private class FreshPantsData {
        private final int pantsColor;
        private final ChatColor textColor;

        public FreshPantsData(int pantsColor, ChatColor textColor) {
            this.pantsColor = pantsColor;
            this.textColor = textColor;
        }

        public int getPantsColor() {
            return pantsColor;
        }

        public ChatColor getTextColor() {
            return textColor;
        }
    }

    private enum FreshPantsColor {
        RED, GREEN, BLUE, YELLOW, ORANGE, DARK, SEWER, AQUA
    }

    private enum HandheldFreshItem {
        SWORD, BOW
    }

    @Override
    public String[] getCommandNames() {
        return new String[] { "givefreshitem" };
    }

    @Override
    public String getUsageMessage(String cmd) {
        return formatStandardUsageMessage(cmd,
                "Gives a fresh item. Must be: sword, bow, or any pants color. (Ex: /givefreshitem red)", "<type>");
    }

    @Override
    public void runCommand(Player player, String commandName, String[] args) {
        String freshItemName = args[0].toUpperCase();

        if (handheldFreshItems.contains(freshItemName)) {
            giveFreshHandheldItem(player, HandheldFreshItem.valueOf(freshItemName));
        } else if (freshPantsColors.contains(freshItemName)) {
            giveFreshPants(player, FreshPantsColor.valueOf(freshItemName));
        }

        player.sendMessage(getUsageMessage(commandName));
    }

    private void giveFreshHandheldItem(Player player, HandheldFreshItem handheldFreshItem) {
        ArrayList<String> freshItemLore = new LoreParser("Kept on death<br/><br/>Used in the mystic well").parse();

        ItemStack freshItem = handheldFreshItem == HandheldFreshItem.SWORD ? new ItemStack(Material.BOW)
                : new ItemStack(Material.GOLDEN_SWORD);

        ItemMeta meta = freshItem.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "Mystic Bow");
        meta.setLore(freshItemLore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);

        freshItem.setItemMeta(meta);

        player.getInventory().addItem(freshItem);
        player.updateInventory();
    }

    private void giveFreshPants(Player player, FreshPantsColor pantsColor) {
        ItemStack freshLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta freshPantsMeta = (LeatherArmorMeta) freshLeggings.getItemMeta();
        String pantsColorName = pantsColor.toString().toLowerCase();

        FreshPantsData data = pantsData.get(pantsColor);

        ChatColor textColor = data.getTextColor();
        int color = data.getPantsColor();

        freshPantsMeta.setColor(Color.fromRGB(color));

        freshPantsMeta.setDisplayName(textColor + "Fresh " + pantsColorName.substring(0, 1).toUpperCase()
                + pantsColorName.substring(1) + " Pants");

        freshPantsMeta.setUnbreakable(true);

        LoreParser loreParser = new LoreParser(
                "Kept on death</br></br>{0}Used in the mystic well{1}</br>{0}Also, a fashion statement{1}");

        String[] variables = new String[2];
        variables[0] = "<" + textColor + ">";
        variables[1] = "</" + textColor + ">";

        loreParser.setVariables(variables);

        freshPantsMeta.setLore(loreParser.parse());

        freshLeggings.setItemMeta(freshPantsMeta);

        player.getInventory().addItem(freshLeggings);
    }
}