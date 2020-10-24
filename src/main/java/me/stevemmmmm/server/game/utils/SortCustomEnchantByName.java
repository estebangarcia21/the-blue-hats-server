package me.stevemmmmm.server.game.utils;

import java.util.Comparator;

import me.stevemmmmm.server.game.enchants.CustomEnchant;

public class SortCustomEnchantByName implements Comparator<CustomEnchant> {
    @Override
    public int compare(CustomEnchant a, CustomEnchant b) {
        try {
            return a.getName().compareTo(b.getName());
        } catch (NullPointerException exception) {
            System.out.println("Error! Missing name information for an enchant.");
        }

        return 0;
    }
}
