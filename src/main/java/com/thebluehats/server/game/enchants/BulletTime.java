package com.thebluehats.server.game.enchants;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger;
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EntityValidator;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class BulletTime extends DamageTriggeredEnchant {
    private final EnchantProperty<Integer> healingAmount = new EnchantProperty<>(0, 2, 3);

    private final DamageManager damageManager;

    @Inject
    public BulletTime(DamageManager damageManager, ArrowDamageTrigger arrowDamageTrigger) {
        super(new DamageEnchantTrigger[] { arrowDamageTrigger }, new EntityValidator[] { damageManager });

        this.damageManager = damageManager;
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        EntityDamageByEntityEvent event = data.getEvent();
        Player damagee = data.getDamagee();
        Arrow arrow = (Arrow) event.getDamager();

        if (damagee.isBlocking()) {
            damageManager.setEventAsCanceled(event);

            arrow.setKnockbackStrength(0);
            arrow.setBounce(true);

            damagee.getWorld().playSound(damagee.getLocation(), Sound.FIZZ, 1f, 1.5f);
            arrow.getWorld().playEffect(arrow.getLocation(), Effect.EXPLOSION, 0, 30);

            arrow.remove();

            damagee.setHealth(Math.min(damagee.getHealth() + healingAmount.getValueAtLevel(data.getLevel()),
                    damagee.getMaxHealth()));
        }
    }

    @Override
    public String getName() {
        return "Bullet Time";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Bullettime";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return null;
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
        return EnchantHolder.DAMAGEE;
    }
}
