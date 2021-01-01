package com.thebluehats.server.game.enchants;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.CalculationMode;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.HitCounter;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import com.thebluehats.server.game.utils.EntityValidator;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ComboDamage extends DamageTriggeredEnchant {
    private final EnchantProperty<Float> damageAmount = new EnchantProperty<>(.2f, .3f, .45f);
    private final EnchantProperty<Integer> hitsNeeded = new EnchantProperty<>(4, 3, 3);

    private final DamageManager damageManager;
    private final HitCounter hitCounter;

    @Inject
    public ComboDamage(DamageManager damageManager, HitCounter hitCounter,
            PlayerDamageTrigger playerDamageTrigger) {
        super(new DamageEnchantTrigger[] { playerDamageTrigger }, new EntityValidator[] { damageManager });

        this.damageManager = damageManager;
        this.hitCounter = hitCounter;
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        Player damager = data.getDamager();

        hitCounter.addOne(damager);

        if (hitCounter.hasHits(damager, hitsNeeded.getValueAtLevel(data.getLevel()))) {
            damager.playSound(damager.getLocation(), Sound.DONKEY_HIT, 1, 0.5f);
            damageManager.addDamage(data.getEvent(), damageAmount.getValueAtLevel(data.getLevel()),
                    CalculationMode.ADDITIVE);
        }
    }

    @Override
    public String getName() {
        return "Combo: Damage";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Combodamage";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Every <yellow>{0}</yellow> strike deals<br/><red>{1}</red> damage");

        String[][] variables = new String[2][];
        variables[0] = new String[] { "fourth", "third", "third" };
        variables[1] = new String[] { "+20%", "+30%", "+45%" };

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
