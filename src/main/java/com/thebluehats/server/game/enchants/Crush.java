package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.Timer;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Crush extends DamageTriggeredEnchant {
    private final EnchantProperty<Integer> weaknessAmplifier = new EnchantProperty<>(4, 5, 6);
    private final EnchantProperty<Integer> weaknessDuration = new EnchantProperty<>(4, 8, 10);

    private final Timer<Player> timer;

    @Inject
    public Crush(Timer<Player> timer, PlayerDamageTrigger playerDamageTrigger) {
        super(new DamageEnchantTrigger[] { playerDamageTrigger });

        this.timer = timer;
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        Player damager = data.getDamager();
        Player damagee = data.getDamagee();
        int level = data.getLevel();

        if (!timer.isRunning(damager)) {
            damagee.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
                    weaknessDuration.getValueAtLevel(level), weaknessAmplifier.getValueAtLevel(level)), true);
        }

        timer.start(damager, 40, false);
    }

    @Override
    public String getName() {
        return "Crush";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Crush";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Strikes apply <red>Weakness {0}</red><br/>(lasts, {1}s, 2s cooldown)");

        String[][] variables = new String[2][];
        variables[0] = new String[] { "V", "VI", "VII" };
        variables[1] = new String[] { "0.2", "0.4", "0.5" };
        
        enchantLoreParser.setVariables(variables);

        return enchantLoreParser.parseForLevel(level);
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.A;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.GOLD_SWORD };
    }

    @Override
    public EnchantHolder getEnchantHolder() {
        return EnchantHolder.DAMAGER;
    }
}
