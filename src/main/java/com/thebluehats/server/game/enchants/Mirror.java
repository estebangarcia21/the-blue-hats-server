package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.core.modules.annotations.AllEventTemplates;
import com.thebluehats.server.game.managers.combat.templates.EventTemplate;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.utils.LoreBuilder;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Mirror extends CustomEnchant<Integer> {
    @Inject
    public Mirror(@AllEventTemplates EventTemplate[] templates) {
        super(templates);
    }

    @Override
    public void execute(Integer arg) {
    }

    @Override
    public String getName() {
        return "Mirror";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Mirror";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("", "25%", "50%").setWriteCondition(level == 1)
                .write("You are immune to true damage").setWriteCondition(level != 1)
                .write("You do not take true damage and").next().write("instead reflect ").setColor(ChatColor.YELLOW)
                .writeVariable(0, level).resetColor().write(" of it to").next().write("your attacker").build();
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.B;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.LEATHER_LEGGINGS };
    }
}
