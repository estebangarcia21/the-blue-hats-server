package com.thebluehats.server.game.commands;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.utils.LoreParser;
import com.thebluehats.server.game.utils.PantsDataContainer;
import com.thebluehats.server.game.utils.PantsDataContainer.FreshPantsColor;
import com.thebluehats.server.game.utils.PantsDataContainer.PantsData;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class GiveFreshItemCommand extends GameCommand {
    private final ArrayList<String> freshPantsColors = new ArrayList<>();
    private final ArrayList<String> handheldFreshItems = new ArrayList<>();

    private final PantsDataContainer pantsDataContainer;

    @Inject
    public GiveFreshItemCommand(PantsDataContainer pantsDataContainer) {
        this.pantsDataContainer = pantsDataContainer;

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

        applyFlags(meta);

        player.getInventory().addItem(freshItem);
        player.updateInventory();
    }

    private void giveFreshPants(Player player, FreshPantsColor pantsColor) {
        ItemStack freshLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta freshPantsMeta = (LeatherArmorMeta) freshLeggings.getItemMeta();
        String pantsColorName = pantsColor.toString().toLowerCase();

        PantsData data = pantsDataContainer.getData().get(pantsColor);

        ChatColor textColor = data.getTextColor();
        int color = data.getPantsColor();

        freshPantsMeta.setColor(Color.fromRGB(color));

        freshPantsMeta.setDisplayName(textColor + "Fresh " + pantsColorName.substring(0, 1).toUpperCase()
                + pantsColorName.substring(1) + " Pants");

        applyFlags(freshPantsMeta);

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

    private void applyFlags(ItemMeta meta) {
        meta.setUnbreakable(true);

        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS,
                new AttributeModifier("GENERIC_ARMOR_TOUGHNESS", 3, AttributeModifier.Operation.ADD_NUMBER));
    }
}