package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.thebluehats.server.game.utils.LoreBuilder;

public class Mirror extends CustomEnchant {
    public final EnchantProperty<Float> damageReflection = new EnchantProperty<>(0f, .25f, .5f);

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
