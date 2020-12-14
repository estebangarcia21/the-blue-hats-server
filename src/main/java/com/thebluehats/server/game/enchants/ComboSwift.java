package com.thebluehats.server.game.enchants;

import java.util.ArrayList;
import java.util.UUID;

import com.google.inject.Inject;
import com.thebluehats.server.game.enchants.processedevents.PostEventTemplateResult;
import com.thebluehats.server.game.managers.combat.templates.PlayerHitPlayerTemplate;
import com.thebluehats.server.game.managers.combat.templates.TargetPlayer;
import com.thebluehats.server.game.managers.enchants.DamageEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.HitCounter;
import com.thebluehats.server.game.utils.LoreBuilder;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ComboSwift implements DamageEnchant {
    private final EnchantProperty<Integer> speedTime = new EnchantProperty<>(3, 4, 5);
    private final EnchantProperty<Integer> speedAmplifier = new EnchantProperty<>(0, 1, 1);
    private final EnchantProperty<Integer> hitsNeeded = new EnchantProperty<>(4, 3, 3);

    private final HitCounter hitCounter;
    private final PlayerHitPlayerTemplate playerHitPlayerTemplate;

    @Inject
    public ComboSwift(HitCounter hitCounter, PlayerHitPlayerTemplate playerHitPlayerTemplate) {
        this.hitCounter = hitCounter;
        this.playerHitPlayerTemplate = playerHitPlayerTemplate;
    }

    @Override
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        playerHitPlayerTemplate.run(this, event, TargetPlayer.DAMAGER, PlayerInventory::getItemInMainHand);
    }

    @Override
    public void execute(PostEventTemplateResult data, int level) {
        Player player = data.getDamager();
        UUID playerUuid = player.getUniqueId();

        hitCounter.addOne(playerUuid);

        if (hitCounter.hasHits(player, hitsNeeded.getValueAtLevel(level))) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, speedTime.getValueAtLevel(level) * 20,
                    speedAmplifier.getValueAtLevel(level), true));
        }
    }

    @Override
    public String getName() {
        return "Combo: Swift";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Comboswift";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("fourth", "third", "third")
                .declareVariable("Speed I", "Speed II", "Speed III").declareVariable("3", "4", "5").write("Every ")
                .writeVariable(ChatColor.YELLOW, 0, level).write(" strike gain").next()
                .writeVariable(ChatColor.YELLOW, 1, level).write(" (").writeVariable(2, level).write("s)").build();
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
        return new Material[] { Material.GOLDEN_SWORD };
    }
}
