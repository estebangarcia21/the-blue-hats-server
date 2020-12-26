package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.processedevents.PostDamageEventTemplateResult;
import com.thebluehats.server.game.managers.combat.CalculationMode;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.TargetPlayer;
import com.thebluehats.server.game.managers.enchants.DamageEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.HitCounter;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ComboDamage implements DamageEnchant {
    private final EnchantProperty<Float> damageAmount = new EnchantProperty<>(.2f, .3f, .45f);
    private final EnchantProperty<Integer> hitsNeeded = new EnchantProperty<>(4, 3, 3);

    private final DamageManager damageManager;
    private final HitCounter hitCounter;
    private final PlayerHitPlayerTemplate playerHitPlayerTemplate;

    @Inject
    public ComboDamage(DamageManager damamgeManager, HitCounter hitCounter,
            PlayerHitPlayerTemplate playerHitPlayerTemplate) {
        this.damageManager = damamgeManager;
        this.hitCounter = hitCounter;
        this.playerHitPlayerTemplate = playerHitPlayerTemplate;
    }

    @Override
    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        playerHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGER, damageManager);
    }

    @Override
    public void execute(PostDamageEventTemplateResult data) {
        Player damager = data.getDamager();

        hitCounter.addOne(damager);

        if (hitCounter.hasHits(damager, hitsNeeded.getValueAtLevel(data.getPrimaryLevel()))) {
            damager.playSound(damager.getLocation(), Sound.ENTITY_DONKEY_HURT, 1, 0.5f);
            damageManager.addDamage(data.getEvent(), damageAmount.getValueAtLevel(data.getPrimaryLevel()),
                    CalculationMode.ADDITIVE);
        }
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
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Every <yellow>{0}</yellow> strike deals<br/><red>{1}</red> damage");

        String[][] variables = new String[2][];
        variables[0] = new String[] { "fourth", "third", "third" };
        variables[1] = new String[] { "+20%", "+30%", "+45%" };

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
}
