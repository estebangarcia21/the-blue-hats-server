package com.thebluehats.server.game.enchants;

import java.util.ArrayList;
import java.util.UUID;

import com.thebluehats.server.game.enchants.args.common.PlayerAndDamageEventArgs;
import com.thebluehats.server.game.managers.combat.DamageManager;
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

public class CriticallyFunky extends CustomEnchant<PlayerAndDamageEventArgs> {
    private final EnchantProperty<Float> DAMAGE_REDUCTION = new EnchantProperty<>(0.35f, 0.35f, 0.6f);
    private final EnchantProperty<Float> DAMAGE_INCREASE = new EnchantProperty<>(0f, .14f, .3f);
    private final ArrayList<UUID> EXTRA_DAMAGE_QUEUE = new ArrayList<>();

    private DamageManager manager;

    public CriticallyFunky(DamageManager manager, EventTemplate[] templates) {
        super(templates);

        this.manager = manager;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        runEventTemplates(this, event.getDamager(), event.getEntity(), PlayerInventory::getItemInMainHand,
                level -> execute(new PlayerAndDamageEventArgs((Player) event.getDamager(), event)));
    }

    @Override
    public void execute(PlayerAndDamageEventArgs args) {
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
    }

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
