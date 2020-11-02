package com.thebluehats.server.game.enchants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.transform.Templates;

import com.thebluehats.server.game.managers.combat.CalculationMode;
import com.thebluehats.server.game.managers.combat.DamageManager;
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
import org.bukkit.inventory.PlayerInventory;

public class CriticallyFunky extends CustomEnchant {
    private final EnchantProperty<Float> damageReduction = new EnchantProperty<>(0.35f, 0.35f, 0.6f);
    private final EnchantProperty<Float> damageIncrease = new EnchantProperty<>(0f, .14f, .3f);

    private final List<UUID> queue = new ArrayList<>();
    private DamageManager manager;

    public CriticallyFunky(DamageManager manager, EventTemplate... templates) {
        super(templates);

        this.manager = manager;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        runEventTemplates(this, event.getDamager(), event.getEntity(), PlayerInventory::getItemInMainHand,
                level -> executeEnchant((Player) event.getDamager(), level, event));
    }

    public void executeEnchant(Player damager, int level, EntityDamageByEntityEvent event) {
    }
    // if (event.getDamager() instanceof Arrow) {
    // Arrow arrow = (Arrow) event.getDamager();

    // if (!arrow.isCritical())
    // return;
    // } else if (damager instanceof Player) {
    // damager = (Player) damager;
    // }

    // if (damager == null)
    // return;

    // if (!manager.isCriticalHit(damager))
    // return;

    // if (queue.contains(damager.getUniqueId())) {
    // manager.addDamage(event, damageIncrease.getValueAtLevel(level),
    // CalculationMode.ADDITIVE);
    // queue.remove(damager.getUniqueId());
    // }

    // if (level != 1) {
    // queue.add(event.getEntity().getUniqueId());
    // }

    // manager.reduceDamage(event, damageReduction.getValueAtLevel(level));
    // manager.removeExtraCriticalDamage(event);
    // }

    @Override
    public String getName() {
        return "Critically Funky";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Critfunky";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("65%", "65%", "40%").declareVariable("", "14%", "30%")
                .write("Critical hits against you deal").next().setColor(ChatColor.BLUE).writeVariable(0, level)
                .resetColor().write(" of the damage they").next().write("normally would").setWriteCondition(level != 1)
                .write(" and empower your").next().write("next strike for ").setColor(ChatColor.RED)
                .writeVariable(1, level).resetColor().write(" damage").build();
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
