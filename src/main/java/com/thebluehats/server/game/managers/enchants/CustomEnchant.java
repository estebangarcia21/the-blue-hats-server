package com.thebluehats.server.game.managers.enchants;

import java.util.ArrayList;

import org.bukkit.Material;

public interface CustomEnchant {
    String getName();

    String getEnchantReferenceName();

    ArrayList<String> getDescription(int level);

    boolean isDisabledOnPassiveWorld();

    EnchantGroup getEnchantGroup();

    boolean isRareEnchant();

    Material[] getEnchantItemTypes();
}