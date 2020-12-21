package com.thebluehats.server.game.utils;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

public class EnchantLoreParser extends LoreParserBase<String[][]> {
    private final ArrayList<String> appensions = new ArrayList<>();

    private String[][] variableMatrix;
    private String[] singleVariable;
    private boolean onlyOneVariable;

    private int level = 1;

    public EnchantLoreParser(String lore) {
        super(lore);
    }

    public void addTextIf(boolean condition, String text) {
        if (!condition)
            return;

        appensions.add(text);
    }

    public ArrayList<String> parseForLevel(int level) {
        this.level = level;

        String finalLore = lore;

        for (String appension : appensions) {
            finalLore += appension;
        }

        return parseLore(finalLore);
    }

    public void setSingleVariable(String levelOne, String levelTwo, String levelThree) {
        onlyOneVariable = true;

        singleVariable = new String[] { levelOne, levelTwo, levelThree };
    }

    @Override
    public void setVariables(String[][] variables) {
        variableMatrix = variables;
    }

    @Override
    protected String insertVariableValuesForLine(String line) {
        if (variableMatrix == null && singleVariable == null)
            return line;

        String formattedLine = line;

        if (onlyOneVariable) {
            formattedLine = StringUtils.replace(formattedLine, "{0}", singleVariable[level - 1]);
        } else {
            for (int i = 0; i < variableMatrix.length; i++) {
                String variable = onlyOneVariable ? singleVariable[level - 1] : variableMatrix[i][level - 1];

                formattedLine = StringUtils.replace(formattedLine, "{" + i + "}", variable);
            }
        }

        return formattedLine;
    }
}
