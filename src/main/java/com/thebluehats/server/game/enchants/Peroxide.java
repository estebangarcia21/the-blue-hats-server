package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.enchants.args.PotionEffectArgs;
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

import java.util.ArrayList;

public class Peroxide extends CustomEnchant<PotionEffectArgs>  {
    private final EnchantProperty<Integer> REGEN_DURATION = new EnchantProperty<>(5, 8, 8);
    private final EnchantProperty<Integer> REGEN_AMPLIFIER = new EnchantProperty<>(0, 0, 1);

    public Peroxide(EventTemplate... templates) {
        super(templates);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        runEventTemplates(this, event.getDamager(), event.getEntity(), PlayerInventory::getLeggings,
                level -> execute(new PotionEffectArgs((Player) event.getEntity(), REGEN_DURATION.getValueAtLevel(level),
                        REGEN_AMPLIFIER.getValueAtLevel(level))));
    }

    @Override
    public void execute(PotionEffectArgs args) {
        args.getPlayer().addPotionEffect(
                new PotionEffect(PotionEffectType.REGENERATION, args.getDuration() * 20, args.getAmplifier(), true));
    }

    @Override
    public String getName() {
        return "Peroxide";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Peroxide";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("Regen I", "Regen I", "Regen II").declareVariable("5", "8", "8")
                .write("Gain ").writeVariable(ChatColor.RED, 0, level).write(" (").writeVariable(1, level).write("s)")
                .write(" when hit").build();
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
        return new Material[] { Material.LEATHER_LEGGINGS };
    }
}