package com.thebluehats.server.game.enchants;

import java.util.ArrayList;
import java.util.UUID;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.processedevents.PostDamageEventTemplateResult;
import com.thebluehats.server.game.managers.combat.CalculationMode;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.TargetPlayer;
import com.thebluehats.server.game.managers.enchants.OnDamageEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.EnchantLoreParser;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CriticallyFunky implements OnDamageEnchant {
    private final EnchantProperty<Float> damageReduction = new EnchantProperty<>(0.35f, 0.35f, 0.6f);
    private final EnchantProperty<Float> damageIncrease = new EnchantProperty<>(0f, .14f, .3f);
    private final ArrayList<UUID> extraDamageQueue = new ArrayList<>();

    private final DamageManager damageManager;
    private final PlayerHitPlayerTemplate playerHitPlayerTemplate;
    private final ArrowHitPlayerTemplate arrowHitPlayerTemplate;

    @Inject
    public CriticallyFunky(DamageManager damageManager, PlayerHitPlayerTemplate playerHitPlayerTemplate,
            ArrowHitPlayerTemplate arrowHitPlayerTemplate) {
        this.damageManager = damageManager;
        this.playerHitPlayerTemplate = playerHitPlayerTemplate;
        this.arrowHitPlayerTemplate = arrowHitPlayerTemplate;
    }

    @Override
    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        playerHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGER, damageManager);
        arrowHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGER, damageManager);
    }

    @Override
    public void execute(PostDamageEventTemplateResult data) {
        EntityDamageByEntityEvent event = data.getEvent();
        Player damager = data.getDamager();
        int level = data.getPrimaryLevel();

        if (!damageManager.isCriticalHit(damager))
            return;

        if (extraDamageQueue.contains(damager.getUniqueId())) {
            damageManager.addDamage(event, damageIncrease.getValueAtLevel(level), CalculationMode.ADDITIVE);
            extraDamageQueue.remove(damager.getUniqueId());
        }

        if (level != 1) {
            extraDamageQueue.add(event.getEntity().getUniqueId());
        }

        damageManager.reduceDamageByPercentage(event, damageReduction.getValueAtLevel(level));
        damageManager.removeExtraCriticalDamage(event);
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
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser(
                "Critical hits against you deal<br/><blue>{0}</blue> of the damage they<br/> normally would");

        enchantLoreParser.addTextIf(level != 1, " and empower your<br/>next strike for <red>{2}</red> damage");

        String[][] variables = new String[2][];
        variables[0] = new String[] { "65%", "65%", "40%" };
        variables[1] = new String[] { "", "14%", "30%" };

        enchantLoreParser.setVariables(variables);

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
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.LEATHER_LEGGINGS };
    }
}
