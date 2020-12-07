package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.combat.templates.EventTemplate;
import com.thebluehats.server.game.utils.LoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;

public class Healer extends CustomEnchant<HealerArgs> {
    private final EnchantProperty<Integer> HEAL_AMOUNT = new EnchantProperty<>(2, 4, 6);

    public Healer(EventTemplate[] templates) {
        super(templates);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        runEventTemplates(this, event.getDamager(), event.getEntity(), PlayerInventory::getItemInMainHand,
                level -> execute(new HealerArgs((Player) event.getDamager(), (Player) event.getEntity(),
                        HEAL_AMOUNT.getValueAtLevel(level))));
    }


    @Override
    public void execute(HealerArgs args) {
        Player damager = args.getDamager();
        Player damaged = args.getDamaged();
        int healAmount = args.getHealAmount();

        damager.setHealth(Math.min(damager.getHealth() + healAmount,
                damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
        damaged.setHealth(Math.min(damaged.getHealth() + healAmount,
                damaged.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
    }

    @Override
    public String getName() {
        return "Healer";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Healer";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("1❤", "2❤", "3❤").write("Hitting a player ").setColor(ChatColor.GREEN)
                .write("heals").resetColor().write(" both you and them for ").setColor(ChatColor.RED)
                .writeVariable(0, level).build();
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return true;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.B;
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.GOLDEN_SWORD };
    }
}

class HealerArgs {
    private Player damager;
    private Player damaged;
    private int healAmount;

    public HealerArgs(Player damager, Player damaged, int healAmount) {
        this.damager = damager;
        this.damaged = damaged;
        this.healAmount = healAmount;
    }

    public Player getDamager() {
        return damager;
    }

    public Player getDamaged() {
        return damaged;
    }

    public int getHealAmount() {
        return healAmount;
    }
}