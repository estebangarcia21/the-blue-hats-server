package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;

public class Mirror implements CustomEnchant {
    @Override
    public String getName() {
        return "Mirror";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Mirror";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("");

        enchantLoreParser.addTextIf(level == 1, "You are immune to true damage");
        enchantLoreParser.addTextIf(level != 1,
                "You do not take true damage and<br/>instead reflect <yellow>{0} of it to<br/>your attacker");

        enchantLoreParser.setSingleVariable("", "25%", "50%");

        return enchantLoreParser.parseForLevel(level);
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.B;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.LEATHER_LEGGINGS };
    }
}
