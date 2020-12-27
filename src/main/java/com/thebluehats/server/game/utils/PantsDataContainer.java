package com.thebluehats.server.game.utils;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;

public class PantsDataContainer {
    public enum FreshPantsColor {
        RED, GREEN, BLUE, YELLOW, ORANGE, DARK, SEWER, AQUA
    }

    public static class PantsData {
        private final int pantsColor;
        private final ChatColor textColor;

        public PantsData(int pantsColor, ChatColor textColor) {
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

    private final ImmutableMap<FreshPantsColor, PantsData> pantsData = ImmutableMap
            .<FreshPantsColor, PantsData>builder().put(FreshPantsColor.RED, new PantsData(0xFF5555, ChatColor.RED))
            .put(FreshPantsColor.GREEN, new PantsData(0x55FF55, ChatColor.GREEN))
            .put(FreshPantsColor.BLUE, new PantsData(0x5555FF, ChatColor.BLUE))
            .put(FreshPantsColor.YELLOW, new PantsData(0xFFFF55, ChatColor.YELLOW))
            .put(FreshPantsColor.ORANGE, new PantsData(0xFFAA00, ChatColor.GOLD))
            .put(FreshPantsColor.DARK, new PantsData(0x000000, ChatColor.DARK_PURPLE))
            .put(FreshPantsColor.SEWER, new PantsData(0x7DC383, ChatColor.DARK_AQUA))
            .put(FreshPantsColor.AQUA, new PantsData(0x55FFFF, ChatColor.DARK_AQUA)).build();

    public ImmutableMap<FreshPantsColor, PantsData> getData() {
        return pantsData;
    }
}
