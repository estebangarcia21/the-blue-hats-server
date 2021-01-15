package com.thebluehats.server.game.enchants;

import java.util.ArrayList;
import java.util.UUID;

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

public class SpeedyHit extends DamageTriggeredEnchant {
    private final EnchantProperty<Integer> speedDuration = new EnchantProperty<>(5, 7, 9);
    private final EnchantProperty<Integer> cooldownTime = new EnchantProperty<>(3, 2, 1);

    private final Timer<UUID> timer;

    @Inject
    public SpeedyHit(Timer<UUID> timer, PlayerDamageTrigger playerDamageTrigger) {
        super(new DamageEnchantTrigger[] { playerDamageTrigger });

        this.timer = timer;
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        Player damager = data.getDamager();
        UUID playerUuid = damager.getUniqueId();
        int level = data.getLevel();

        if (!timer.isRunning(playerUuid)) {
            damager.addPotionEffect(
                    new PotionEffect(PotionEffectType.SPEED, speedDuration.getValueAtLevel(level) * 20, 0, true));
        }

        timer.start(playerUuid, cooldownTime.getValueAtLevel(level) * 20, false);
    }

    @Override
    public String getName() {
        return "Speedy Hit";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Speedyhit";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Gain Speed I for <yellow>{0}s</yellow> on hit({1}s<br/>cooldown)");

        String[][] variables = new String[2][];
        variables[0] = new String[] { "5", "7", "9" };
        variables[1] = new String[] { "3", "2", "1" };

        enchantLoreParser.setVariables(variables);

        return enchantLoreParser.parseForLevel(level);
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        // TODO Determine EnchantGroup
        return EnchantGroup.B;
    }

    @Override
    public boolean isRareEnchant() {
        return true;
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
