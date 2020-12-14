package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.enchants.processedevents.PostEventTemplateResult;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.TargetPlayer;
import com.thebluehats.server.game.managers.enchants.DamageEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.LoreBuilder;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SprintDrain implements DamageEnchant {
    private final EnchantProperty<Integer> speedDuration = new EnchantProperty<>(5, 5, 7);
    private final EnchantProperty<Integer> speedAmplifier = new EnchantProperty<>(0, 0, 1);

    private final ArrowHitPlayerTemplate arrowHitPlayerTemplate;

    @Inject
    public SprintDrain(ArrowHitPlayerTemplate arrowHitPlayerTemplate) {
        this.arrowHitPlayerTemplate = arrowHitPlayerTemplate;
    }

    @Override
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        arrowHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGER, PlayerInventory::getItemInMainHand);
    }

    @Override
    public void execute(PostEventTemplateResult data, int level) {
        data.getDamager().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                speedDuration.getValueAtLevel(level) * 20, speedAmplifier.getValueAtLevel(level)));

        if (level == 1)
            return;

        data.getDamagee().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 0));
    }

    @Override
    public String getName() {
        return "Sprint Drain";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Sprintdrain";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("I", "I", "II").declareVariable("", "5", "7")
                .write("Arrow shots grant you ").write(ChatColor.YELLOW, "Speed ")
                .writeVariable(ChatColor.YELLOW, 0, level).next().write("(").writeVariable(1, level).write("s)")
                .setWriteCondition(level != 1).write(" and apply ").write(ChatColor.BLUE, "Slowness I").next()
                .write("(3s)").build();
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
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.BOW };
    }
}