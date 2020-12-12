package com.thebluehats.server.game.utils;

import java.util.Comparator;

import com.thebluehats.server.game.managers.enchants.CustomEnchant;

public class SortCustomEnchantByName implements Comparator<CustomEnchant<?>> {
    @Override
    public int compare(CustomEnchant<?> a, CustomEnchant<?> b) {
        try {
            return a.getName().compareTo(b.getName());
        } catch (NullPointerException exception) {
            System.out.println("Error! Missing name information for an enchant.");
        }

        return 0;
    }
}
