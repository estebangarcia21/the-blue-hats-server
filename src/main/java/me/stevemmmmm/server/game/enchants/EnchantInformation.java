package me.stevemmmmm.server.game.enchants;

import java.util.ArrayList;

import org.bukkit.Material;

public interface EnchantInformation {
    public abstract void applyEnchant(int level, Object... args);

    public abstract String getName();

    public abstract String getEnchantReferenceName();

    public abstract ArrayList<String> getDescription(int level);

    public abstract boolean isDisabledOnPassiveWorld();

    public abstract EnchantGroup getEnchantGroup();

    public abstract boolean isRareEnchant();

    public abstract Material[] getEnchantItemTypes();
}
