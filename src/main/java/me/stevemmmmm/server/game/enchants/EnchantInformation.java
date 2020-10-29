package me.stevemmmmm.server.game.enchants;

import java.util.ArrayList;

import org.bukkit.Material;

public interface EnchantInformation {
    String getName();

    String getEnchantReferenceName();

    ArrayList<String> getDescription(int level);

    boolean isDisabledOnPassiveWorld();

    EnchantGroup getEnchantGroup();

    boolean isRareEnchant();

    Material[] getEnchantItemTypes();
}
