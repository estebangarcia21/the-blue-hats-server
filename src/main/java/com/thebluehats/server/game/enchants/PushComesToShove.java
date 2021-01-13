package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger;
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.HitCounter;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import javax.inject.Inject;
import java.util.ArrayList;

public class PushComesToShove extends DamageTriggeredEnchant {
    private final EnchantProperty<Float> pctsForce = new EnchantProperty<>(12f, 25f, 35f);
    private final EnchantProperty<Float> damageAmount = new EnchantProperty<>(0f, 1f, 2f);

    private final HitCounter hitCounter;

    @Inject
    public PushComesToShove(HitCounter hitCounter, ArrowDamageTrigger arrowDamageTrigger) {
        super(new DamageEnchantTrigger[] { arrowDamageTrigger });

        this.hitCounter = hitCounter;
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        Player damagee = data.getDamagee();
        int level = data.getLevel();

        Arrow arrow = (Arrow) data.getEvent().getDamager();

        hitCounter.addOne(damagee);

        if (hitCounter.hasHits(damagee, 3)) {
            Vector velocity = arrow.getVelocity().normalize().multiply(pctsForce.getValueAtLevel(level) / 2.35);
            velocity.setY(0);

            damagee.setVelocity(velocity);

            damagee.damage(damageAmount.getValueAtLevel(level));
        }
    }

    @Override
    public String getName() {
        return "Push Comes to Shove";
    }

    @Override
    public String getEnchantReferenceName() {
        return "PCTS";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("Every 3rd shot on a player<br/><aqua>{0}</aqua>");

        enchantLoreParser.addTextIf(level != 1, " and deals <red>{1}</red><br/>extra damage");

        String[][] variables = new String[2][];

        variables[0] = new String[] { "Punch", "Punch V", "Punch VII" };
        variables[1] = new String[] { "", "+0.5❤", "+1❤" };

        enchantLoreParser.setVariables(variables);

        return enchantLoreParser.parseForLevel(level);
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
        return new Material[] { Material.BOW };
    }

    @Override
    public EnchantHolder getEnchantHolder() {
        return EnchantHolder.DAMAGER;
    }
}
