package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.game.enchants.processedevents.ProcessedEntityDamageByEntityEvent;
import com.thebluehats.server.game.managers.combat.CalculationMode;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.LoreBuilder;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;

public class BeatTheSpammers extends CustomEnchant {
    private final EnchantProperty<Float> damageAmount = new EnchantProperty<>(.10f, .25f, .40f);

    private final DamageManager damageManager;
    private final PlayerHitPlayerTemplate playerHitPlayerTemplate;

    @Inject
    public BeatTheSpammers(DamageManager damageManager, PlayerHitPlayerTemplate playerHitPlayerTemplate) {
        this.damageManager = damageManager;
        this.playerHitPlayerTemplate = playerHitPlayerTemplate;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        playerHitPlayerTemplate.run(this, event, PlayerInventory::getItemInMainHand, (e, level) -> run(e, level),
                damageManager);
    }

    public void run(ProcessedEntityDamageByEntityEvent processedEvent, int level) {
        if (processedEvent.getDamagee().getInventory().getItemInMainHand().getType() == Material.BOW) {
            damageManager.addDamage(processedEvent.getEvent(), damageAmount.getValueAtLevel(level),
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
        return new LoreBuilder().declareVariable("+10%", "+25%", "+40%").write("Deal ")
                .writeVariable(ChatColor.RED, 0, level).write(" damage vs. players").next().write("holding a bow")
                .build();
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
