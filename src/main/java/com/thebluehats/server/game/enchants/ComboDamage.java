package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

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

public class ComboDamage extends CustomEnchant {
    private final EnchantProperty<Integer> hitsNeeded = new EnchantProperty<>(4, 3, 3);
    private final EnchantProperty<Float> damageAmount = new EnchantProperty<>(.2f, .3f, .45f);
    private DamageManager manager;

    public ComboDamage(DamageManager manager, EventTemplate... templates) {
        super(templates);

        this.manager = manager;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        runEventTemplates(this, event.getDamager(), event.getEntity(), PlayerInventory::getItemInMainHand,
                level -> executeEnchant((Player) event.getDamager(), level, event));

    }

    public void executeEnchant(Player damager, int level, EntityDamageByEntityEvent event) {
        // updateHitCount(damager);

        // if (hasRequiredHits(damager, hitsNeeded.getValueAtLevel(level))) {
        // damager.playSound(damager.getLocation(), Sound.ENTITY_DONKEY_HURT , 1, 0.5f);
        // manager.addDamage(event, damageAmount.getValueAtLevel(level),
        // CalculationMode.ADDITIVE);
        //
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
        return new LoreBuilder().declareVariable("fourth", "third", "third").declareVariable("+20%", "+30%", "+45%")
                .write("Every ").setColor(ChatColor.YELLOW).writeVariable(0, level).resetColor().write(" strike deals")
                .next().setColor(ChatColor.RED).writeVariable(1, level).resetColor().write(" damage").build();
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
        return new Material[] { Material.GOLDEN_SWORD };
    }
}
