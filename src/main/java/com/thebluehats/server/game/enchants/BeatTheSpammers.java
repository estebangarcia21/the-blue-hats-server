package com.thebluehats.server.game.enchants;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.CalculationMode;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.PostDamageEventTemplate;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.OnDamageEnchant;
import com.thebluehats.server.game.managers.enchants.processedevents.PostDamageEventResult;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import org.bukkit.Material;

import java.util.ArrayList;

public class BeatTheSpammers extends OnDamageEnchant {
    private final EnchantProperty<Float> damageAmount = new EnchantProperty<>(.10f, .25f, .40f);

    private final DamageManager damageManager;

    @Inject
    public BeatTheSpammers(DamageManager damageManager, PlayerHitPlayerTemplate playerHitPlayerTemplate) {
        super(new PostDamageEventTemplate[] { playerHitPlayerTemplate });

        this.damageManager = damageManager;
    }

    @Override
    public void execute(PostDamageEventResult data) {
        if (data.getDamagee().getInventory().getItemInMainHand().getType() == Material.BOW) {
            damageManager.addDamage(data.getEvent(), damageAmount.getValueAtLevel(data.getLevel()),
                    CalculationMode.ADDITIVE);
        }
    }

    @Override
    public String getName() {
        return "Beat the Spammers";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Beatthespammers";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Deal <red>{0}</red> damage vs. players<br/>holding a bow");

        enchantLoreParser.setSingleVariable("+10%", "+25%", "+40%");

        return enchantLoreParser.parseForLevel(level);
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

    @Override
    public EnchantHolder getEnchantHolder() {
        return EnchantHolder.DAMAGEE;
    }
}
