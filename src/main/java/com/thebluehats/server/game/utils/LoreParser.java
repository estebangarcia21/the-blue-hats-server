package com.thebluehats.server.game.utils;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

public class LoreParser extends LoreParserBase<String[]> {
    private String[] variables;

    public LoreParser(String lore) {
        super(lore);
    }

    public ArrayList<String> parse() {
        return parseLore(lore);
    }

    public void setVariables(String[] variables) {
        this.variables = variables;
    }

    protected String insertVariableValuesForLine(String line) {
        if (variables == null)
            return line;

        String formattedLine = line;

        for (int i = 0; i < variables.length; i++) {
            formattedLine = StringUtils.replace(formattedLine, "{" + i + "}", variables[i]);
        }

        return formattedLine;
    }
}
