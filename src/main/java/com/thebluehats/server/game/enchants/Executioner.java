package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import com.thebluehats.server.game.utils.EntityValidator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.ArrayList;

public class Executioner extends DamageTriggeredEnchant {
    private final EnchantProperty<Integer> heartsToDie = new EnchantProperty<>(3, 4, 4);

    private final DamageManager damageManager;

    @Inject
    public Executioner(DamageManager damageManager, PlayerDamageTrigger playerDamageTrigger) {
        super(new DamageEnchantTrigger[] { playerDamageTrigger }, new EntityValidator[] { damageManager });

        this.damageManager = damageManager;
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        Player damagee = data.getDamagee();
        int level = data.getLevel();
        Player damager = data.getDamager();
        
        if (damagee.getHealth() - damageManager.getFinalDamageFromEvent(data.getEvent()) / 2 <= heartsToDie.getValueAtLevel(level) && damagee.getHealth() > 0) {
            damagee.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "EXECUTED!" + ChatColor.GRAY + " by "
//                    + PermissionsManager.getInstance().getPlayerRank((Player) args[1]).getNameColor()
                    + damager.getName()
                    + damagee.getName() + ChatColor.GRAY + " (insta-kill below " + ChatColor.RED
                    + heartsToDie.getValueAtLevel(level) / 2 + "❤" + ChatColor.GRAY + ")");
            damagee.getWorld().playSound(damagee.getLocation(), Sound.VILLAGER_DEATH, 1, 0.5f);

            // TODO Add name color in message
            // TODO Add particle

            damagee.setHealth(0);

//            damageManager.safeSetPlayerHealth(damagee, 0);
        }
    }


    @Override
    public String getName() {
        return "Executioner";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Executioner";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("Hitting an enemy below <red>{0}</red> instantly kills them");

        enchantLoreParser.setSingleVariable("1.5❤", "2❤", "2❤");
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
        return EnchantHolder.DAMAGER;
    }
}
