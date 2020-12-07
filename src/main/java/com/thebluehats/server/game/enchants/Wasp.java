package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.enchants.args.PotionEffectArgs;
import com.thebluehats.server.game.managers.combat.BowManager;
import com.thebluehats.server.game.managers.combat.templates.EventTemplate;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.LoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Wasp extends CustomEnchant<PotionEffectArgs> {
    private final EnchantProperty<Integer> WEAKNESS_DURATION = new EnchantProperty<>(6, 11, 16);
    private final EnchantProperty<Integer> WEAKNESS_AMPLIFIER = new EnchantProperty<>(1, 2, 3);

    private BowManager bowManager;

    public Wasp(BowManager bowManager, EventTemplate[] templates) {
        super(templates);

        this.bowManager = bowManager;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        runEventTemplates(this, event.getDamager(), event.getEntity(),
                inventory -> bowManager.getBowFromArrow((Arrow) event.getDamager()),
                level -> execute(new PotionEffectArgs((Player) event.getEntity(),
                        WEAKNESS_DURATION.getValueAtLevel(level), WEAKNESS_AMPLIFIER.getValueAtLevel(level))));
    }

    @EventHandler
    public void onArrowShootEvent(EntityShootBowEvent event) {
        bowManager.onArrowShoot(event);
    }

    @Override
    public void execute(PotionEffectArgs args) {
        args.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, args.getDuration() * 20, args.getAmplifier()));
    }

    @Override
    public String getName() {
        return "Wasp";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Wasp";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("II", "III", "IV").setColor(ChatColor.GRAY).write("Apply ")
                .setColor(ChatColor.RED).write("Weakness ").writeVariable(0, level).setColor(ChatColor.GRAY)
                .write(" (" + WEAKNESS_DURATION.getValueAtLevel(level) + "s) on hit").build();
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
}
