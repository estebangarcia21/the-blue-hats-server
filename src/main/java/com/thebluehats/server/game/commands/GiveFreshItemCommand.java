package com.thebluehats.server.game.commands;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.utils.LoreParser;
import com.thebluehats.server.game.utils.PantsData;
import com.thebluehats.server.game.utils.PantsData.FreshPantsColor;
import com.thebluehats.server.game.utils.PantsData.PantsDataValue;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class GiveFreshItemCommand extends GameCommand {
    private final ArrayList<String> freshPantsColors = new ArrayList<>();
    private final ArrayList<String> handheldFreshItems = new ArrayList<>();

    private final PantsData pantsData;

    @Inject
    public GiveFreshItemCommand(PantsData pantsData) {
        this.pantsData = pantsData;

        for (FreshPantsColor freshPantsColor : FreshPantsColor.values()) {
            freshPantsColors.add(freshPantsColor.toString());
        }

        for (HandheldFreshItem handheldFreshItem : HandheldFreshItem.values()) {
            handheldFreshItems.add(handheldFreshItem.toString());
        }
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
                "Gives a fresh item. Must be: sword, bow, or any pants color. (Ex: /givefreshitem red)", "type");
    }

    @Override
    public void runCommand(Player player, String commandName, String[] args) {
        String freshItemName = args[0].toUpperCase();

        if (handheldFreshItems.contains(freshItemName)) {
            giveFreshHandheldItem(player, HandheldFreshItem.valueOf(freshItemName));
        } else if (freshPantsColors.contains(freshItemName)) {
            giveFreshPants(player, FreshPantsColor.valueOf(freshItemName));
        } else {
            player.sendMessage(getUsageMessage(commandName));
        }
    }

    private void giveFreshHandheldItem(Player player, HandheldFreshItem handheldFreshItem) {
        ArrayList<String> freshItemLore = new LoreParser("Kept on death<br/><br/>Used in the mystic well").parse();

        ItemStack freshItem = handheldFreshItem == HandheldFreshItem.SWORD ? new ItemStack(Material.GOLD_SWORD)
                : new ItemStack(Material.BOW);

        ItemMeta meta = freshItem.getItemMeta();

        String handheldFreshItemString = handheldFreshItem.toString().toLowerCase();
        String handheldFreshItemName = handheldFreshItemString.substring(0, 1).toUpperCase()
                + handheldFreshItemString.substring(1);

        meta.setDisplayName(ChatColor.AQUA + "Mystic " + handheldFreshItemName);
        meta.setLore(freshItemLore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        meta.spigot().setUnbreakable(true);

        applyFlags(meta);

        freshItem.setItemMeta(meta);

        player.getInventory().addItem(freshItem);
        player.updateInventory();
    }

    private void giveFreshPants(Player player, FreshPantsColor pantsColor) {
        ItemStack freshLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta freshPantsMeta = (LeatherArmorMeta) freshLeggings.getItemMeta();
        String pantsColorName = pantsColor.toString().toLowerCase();

        PantsDataValue data = pantsData.getData().get(pantsColor);

        ChatColor textColor = data.getTextColor();
        int color = data.getPantsColor();

        freshPantsMeta.setColor(Color.fromRGB(color));

        freshPantsMeta.setDisplayName(textColor + "Fresh " + pantsColorName.substring(0, 1).toUpperCase()
                + pantsColorName.substring(1) + " Pants");

        applyFlags(freshPantsMeta);

        LoreParser loreParser = new LoreParser(
                "Kept on death<br/><br/>{0}Used in the mystic well{1}<br/>{0}Also, a fashion statement{1}");

        String textColorName = textColor.name().toLowerCase();

        String[] variables = new String[2];
        variables[0] = "<" + textColorName + ">";
        variables[1] = "</" + textColorName + ">";

        loreParser.setVariables(variables);

        freshPantsMeta.setLore(loreParser.parse());

        freshLeggings.setItemMeta(freshPantsMeta);

        player.getInventory().addItem(freshLeggings);
    }

    private void applyFlags(ItemMeta meta) {
        meta.spigot().setUnbreakable(true);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
    }
}