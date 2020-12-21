package com.thebluehats.server.game.utils;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

public abstract class LoreParserBase<T> {
    protected final String lore;

    protected LoreParserBase(String lore) {
        this.lore = lore;
    }

    protected ArrayList<String> parseLore(String lore) {
        ArrayList<String> parsedLore = new ArrayList<>();

        String[] lines = lore.split("<br/>");

        for (String line : lines) {
            String formattedLine = ChatColor.GRAY.toString() + line.trim();

            for (ChatColor chatColor : ChatColor.values()) {
                String name = chatColor.name().toLowerCase();

                formattedLine = StringUtils.replace(formattedLine, "<" + name + ">", chatColor.toString());
                formattedLine = StringUtils.replace(formattedLine, "</" + name + ">", ChatColor.GRAY.toString());
            }

            formattedLine = insertVariableValuesForLine(formattedLine);

            parsedLore.add(formattedLine);
        }

        return parsedLore;
    }

    public abstract void setVariables(T variables);

    protected abstract String insertVariableValuesForLine(String line);
}
