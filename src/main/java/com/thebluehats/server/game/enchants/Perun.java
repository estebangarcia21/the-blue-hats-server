package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.HitCounter;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import com.thebluehats.server.game.utils.EntityValidator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Perun extends DamageTriggeredEnchant {
    private final EnchantProperty<Integer> perunDamage = new EnchantProperty<>(3, 4, 2);
    private final EnchantProperty<Integer> hitsNeeded = new EnchantProperty<>(5, 4, 4);

    private final DamageManager damageManager;
    private final HitCounter hitCounter;

    @Inject
    public Perun(DamageManager damageManager, HitCounter hitCounter, PlayerDamageTrigger playerDamageTrigger) {
        super(new DamageEnchantTrigger[] { playerDamageTrigger }, new EntityValidator[] { damageManager });

        this.damageManager = damageManager;
        this.hitCounter = hitCounter;
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        Player damager = data.getDamager();
        Player damagee = data.getDamagee();
        int level = data.getLevel();

        int damage = perunDamage.getValueAtLevel(level);

        hitCounter.addOne(damager);

        if (hitCounter.hasHits(damager, hitsNeeded.getValueAtLevel(level))) {
            if (level == 3) {
                if (damagee.getInventory().getBoots() != null)
                    if (damagee.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS)
                        damage += 2;
                if (damagee.getInventory().getChestplate() != null)
                    if (damagee.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE)
                        damage += 2;
                if (damagee.getInventory().getLeggings() != null)
                    if (damagee.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS)
                        damage += 2;
                if (damagee.getInventory().getHelmet() != null)
                    if (damagee.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET)
                        damage += 2;
            }

            damager.getWorld().strikeLightningEffect(damagee.getLocation());
            damageManager.doTrueDamage(damagee, damage);
        }
    }

    @Override
    public String getName() {
        return "Combo: Perun's Wrath";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Perun";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("");

        String lastMessage = ChatColor.ITALIC + "Lightning deals true damage";

        enchantLoreParser.addTextIf(level != 3,
                "Every <yellow>fourth</yellow> hit strikes<br/>lightning for <red>{0}❤</red><br/>" + lastMessage);
        enchantLoreParser.addTextIf(level == 3,
                "Every <yellow>fourth</yellow> hit strikes<br/>lightning for <red>{0}❤</red> + <red>1❤</red><br/>per <aqua>diamond piece</aqua> on your<br/> victim<br/>"
                        + lastMessage);

        enchantLoreParser.setSingleVariable("1.5", "2", "1");

        return enchantLoreParser.parseForLevel(level);
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
        return true;
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
