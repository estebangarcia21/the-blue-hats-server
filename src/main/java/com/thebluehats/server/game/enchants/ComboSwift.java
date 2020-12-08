package com.thebluehats.server.game.enchants;

import java.util.ArrayList;

import com.google.inject.Inject;
import com.thebluehats.server.core.modules.annotations.PlayerHitPlayer;
import com.thebluehats.server.game.enchants.args.PotionEffectWithHitsNeededArgs;
import com.thebluehats.server.game.managers.combat.templates.EventTemplate;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.HitCounter;
import com.thebluehats.server.game.utils.LoreBuilder;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ComboSwift extends CustomEnchant<PotionEffectWithHitsNeededArgs> {
    private final EnchantProperty<Integer> speedTime = new EnchantProperty<>(3, 4, 5);
    private final EnchantProperty<Integer> speedAmplifier = new EnchantProperty<>(0, 1, 1);
    private final EnchantProperty<Integer> hitsNeeded = new EnchantProperty<>(4, 3, 3);

    private final HitCounter hitCounter;

    @Inject
    public ComboSwift(HitCounter hitCounter, @PlayerHitPlayer EventTemplate[] templates) {
        super(templates);

        this.hitCounter = hitCounter;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        runEventTemplates(this, event.getDamager(), event.getEntity(), PlayerInventory::getItemInMainHand,
                level -> execute((new PotionEffectWithHitsNeededArgs((Player) event.getDamager(),
                        speedTime.getValueAtLevel(level), speedAmplifier.getValueAtLevel(level),
                        hitsNeeded.getValueAtLevel(level)))));
    }

    @Override
    public void execute(PotionEffectWithHitsNeededArgs args) {
        Player player = args.getPlayer();

        hitCounter.addOne(player);

        if (hitCounter.hasHits(player, args.getHitsNeeded())) {
            player.addPotionEffect(
                    new PotionEffect(PotionEffectType.SPEED, args.getDuration() * 20, args.getAmplifier(), true));
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
