package com.thebluehats.server.game.utils;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;

public class PantsData {
    public enum FreshPantsColor {
        RED, GREEN, BLUE, YELLOW, ORANGE, DARK, SEWER, AQUA
    }

    public static class PantsDataValue {
        private final int pantsColor;
        private final ChatColor textColor;

        public PantsDataValue(int pantsColor, ChatColor textColor) {
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

    private final ImmutableMap<FreshPantsColor, PantsDataValue> pantsData = ImmutableMap
            .<FreshPantsColor, PantsDataValue>builder().put(FreshPantsColor.RED, new PantsDataValue(0xFF5555, ChatColor.RED))
            .put(FreshPantsColor.GREEN, new PantsDataValue(0x55FF55, ChatColor.GREEN))
            .put(FreshPantsColor.BLUE, new PantsDataValue(0x5555FF, ChatColor.BLUE))
            .put(FreshPantsColor.YELLOW, new PantsDataValue(0xFFFF55, ChatColor.YELLOW))
            .put(FreshPantsColor.ORANGE, new PantsDataValue(0xFFAA00, ChatColor.GOLD))
            .put(FreshPantsColor.DARK, new PantsDataValue(0x000000, ChatColor.DARK_PURPLE))
            .put(FreshPantsColor.SEWER, new PantsDataValue(0x7DC383, ChatColor.DARK_AQUA))
            .put(FreshPantsColor.AQUA, new PantsDataValue(0x55FFFF, ChatColor.DARK_AQUA)).build();

    public ImmutableMap<FreshPantsColor, PantsDataValue> getData() {
        return pantsData;
    }
}
