package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.core.modules.annotations.ArrowHitPlayer;
import com.thebluehats.server.game.enchants.args.common.PotionEffectArgs;
import com.thebluehats.server.game.managers.combat.templates.EventTemplate;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.LoreBuilder;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SprintDrain extends CustomEnchant<SprintDrainArgs> {
    private final EnchantProperty<Integer> SPEED_DURATION = new EnchantProperty<>(5, 5, 7);
    private final EnchantProperty<Integer> SPEED_AMPLIFIER = new EnchantProperty<>(0, 0, 1);

    @Inject
    public SprintDrain(@ArrowHitPlayer EventTemplate[] templates) {
        super(templates);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        runEventTemplates(this, event.getDamager(), event.getEntity(), PlayerInventory::getItemInMainHand,
                level -> execute(new SprintDrainArgs((Player) event.getDamager(), (Player) event.getEntity(),
                        SPEED_DURATION.getValueAtLevel(level), SPEED_AMPLIFIER.getValueAtLevel(level), level)));
    }

    @Override
    public void execute(SprintDrainArgs args) {
        args.getPlayer().addPotionEffect(
                new PotionEffect(PotionEffectType.SPEED, args.getDuration() * 20, args.getAmplifier()));

        if (args.getLevel() == 1)
            return;

        args.getDamaged().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 0));
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

class SprintDrainArgs extends PotionEffectArgs {
    private final Player damaged;
    private final int level;

    public SprintDrainArgs(Player damager, Player damaged, int duration, int amplifier, int level) {
        super(damager, duration, amplifier);

        this.damaged = damaged;
        this.level = level;
    }

    public Player getDamaged() {
        return damaged;
    }

    public int getLevel() {
        return level;
    }
}