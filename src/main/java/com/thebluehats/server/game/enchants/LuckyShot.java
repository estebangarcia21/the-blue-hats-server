package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.managers.combat.BowManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowHitPlayerVerifier;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Random;

public class LuckyShot implements CustomEnchant, Listener {
    private final EnchantProperty<Float> percentChance = new EnchantProperty<>(0.02f, 0.05f, 0.1f);
    private final Random random = new Random();

    private final CustomEnchantUtils customEnchantUtils;
    private final BowManager bowManager;
    private final ArrowHitPlayerVerifier arrowHitPlayerVerifier;

    @Inject
    public LuckyShot(CustomEnchantUtils customEnchantUtils, BowManager bowManager, ArrowHitPlayerVerifier arrowHitPlayerVerifier) {
        this.customEnchantUtils = customEnchantUtils;
        this.bowManager = bowManager;
        this.arrowHitPlayerVerifier = arrowHitPlayerVerifier;
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event) {
        if (arrowHitPlayerVerifier.verify(event)) {
            Arrow arrow = (Arrow) event.getDamager();
            Player player = (Player) arrow.getShooter();

            ItemStack bow = bowManager.getBowFromArrow(arrow);

            if (customEnchantUtils.itemHasEnchant(this, bow)) {
                float chance = percentChance.getValueAtLevel(customEnchantUtils.getEnchantLevel(this, bow));
                float randomValue = random.nextFloat();

                if (randomValue <= chance) {
                    player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "LUCKY SHOT!" + ChatColor.LIGHT_PURPLE + " Quadruple damage!");
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Lucky Shot";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Luckyshot";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("<yellow>{0}</yellow> chance for a shot to deal<br/>quadruple damage");

        enchantLoreParser.setSingleVariable("2%", "5%", "10%");

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
        return new Material[] { Material.BOW };
    }
}
