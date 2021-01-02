package com.thebluehats.server.game.enchants;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.enchants.*;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class BooBoo implements CustomEnchant, GlobalTimerListener {
    private final EnchantProperty<Integer> secondsNeeded = new EnchantProperty<>(5, 4, 3);

    private final CustomEnchantUtils customEnchantUtils;
    private final Timer<UUID> timer;

    @Inject
    public BooBoo(CustomEnchantUtils customEnchantUtils, Timer<UUID> timer) {
        this.customEnchantUtils = customEnchantUtils;
        this.timer = timer;
    }

    // TODO Optimize Boo-boo
    @Override
    public void onTick(Player player) {
        ItemStack leggings = player.getInventory().getLeggings();

        if (customEnchantUtils.itemHasEnchant(this, leggings)) {
            timer.start(player.getUniqueId(), secondsNeeded.getValueAtLevel(customEnchantUtils.getEnchantLevel(this, leggings) * 20), () -> execute(player));
        }
    }

    public void execute(Player player) {
        player.setHealth(Math.min(player.getHealth() + 2, player.getMaxHealth()));
    }

    @Override
    public String getName() {
        return "Boo-boo";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Booboo";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser("Passively regain <red>1❤</red> every {0}<br/>seconds");

        enchantLoreParser.setSingleVariable("5", "4", "3");

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
