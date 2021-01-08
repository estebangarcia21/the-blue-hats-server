package com.thebluehats.server.game.enchants;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.HitCounter;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ComboHeal extends DamageTriggeredEnchant {
    private final EnchantProperty<Float> healingAmount = new EnchantProperty<>(.80f, 1.6f, 2.4f);

    private final HitCounter hitCounter;

    @Inject
    public ComboHeal(HitCounter hitCounter, PlayerDamageTrigger playerDamageTrigger) {
        super(new DamageEnchantTrigger[] { playerDamageTrigger });

        this.hitCounter = hitCounter;
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        Player damager = data.getDamager();

        hitCounter.addOne(damager);

        if (hitCounter.hasHits(damager, 4)) {
            EntityPlayer nmsPlayer = ((CraftPlayer) damager).getHandle();

            damager.playSound(damager.getLocation(), Sound.DONKEY_HIT, 1, 0.5f);
            nmsPlayer.setAbsorptionHearts(
                    Math.min(nmsPlayer.getAbsorptionHearts() + healingAmount.getValueAtLevel(data.getLevel()), 8));
        }
    }

    @Override
    public String getName() {
        return "Combo: Heal";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Comboheal";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Every <yellow>fourth</yellow> strike heals<br/><red>{0}❤</red> and grants <gold>{0}❤</gold><br/>absorption");

        enchantLoreParser.setSingleVariable("0.4", "0.8", "1.2");

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
        return new Material[] { Material.GOLD_SWORD };
    }

    @Override
    public EnchantHolder getEnchantHolder() {
        return EnchantHolder.DAMAGER;
    }
}
