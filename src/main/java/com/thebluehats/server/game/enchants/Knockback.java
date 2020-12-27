package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.PostDamageEventTemplate;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.OnDamageEnchant;
import com.thebluehats.server.game.managers.enchants.processedevents.PostDamageEventResult;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Knockback extends OnDamageEnchant {
    private final EnchantProperty<Integer> blocksAmount = new EnchantProperty<>(3, 4, 6);

    @Inject
    public Knockback(PlayerHitPlayerTemplate playerHitPlayerTemplate) {
        super(new PostDamageEventTemplate[] { playerHitPlayerTemplate });
    }

    @Override
    public void execute(PostDamageEventResult data) {
        Player damagee = data.getDamagee();
        int level = data.getLevel();

        damagee.setVelocity(new Vector(damagee.getVelocity().getX() + blocksAmount.getValueAtLevel(level),
                damagee.getVelocity().getY() + blocksAmount.getValueAtLevel(level),
                damagee.getVelocity().getZ() + blocksAmount.getValueAtLevel(level)));
    }

    @Override
    public String getName() {
        return "Knockback";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Knockback";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Increases knockback taken by<br/>enemies by <white>{0} blocks</white>");

        enchantLoreParser.setSingleVariable("3", "6", "9");

        return enchantLoreParser.parseForLevel(level);
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.C;
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.GOLDEN_SWORD };
    }

    @Override
    public EnchantHolder getEnchantHolder() {
        return EnchantHolder.DAMAGER;
    }

}
