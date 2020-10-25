package me.stevemmmmm.server.game.enchants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.stevemmmmm.server.game.managers.DamageManager;
import me.stevemmmmm.server.game.managers.RegionManager;
import me.stevemmmmm.server.game.utils.RomanNumeralConverter;

public abstract class CustomEnchant implements Listener {
    private final RomanNumeralConverter romanNumeralConverter = new RomanNumeralConverter();

    public boolean isCompatibleWith(Material material) {
        for (Material mat : getEnchantItemTypes()) {
            if (mat == material) {
                return true;
            }
        }

        return false;
    }

    public boolean attemptEnchantExecution(EnchantProcData data, Consumer<Integer> execution) {
        if (itemHasEnchant(data.getItemSource(), this))
            return calculateConditions(data, null);

        return false;
    }

    private boolean calculateConditions(EnchantProcData data, Consumer<Integer> execution) {
        DamageManager damageManager = data.getDamageManager();

        for (Entity entity : data.getInteractedEntities()) {
            if (entity instanceof Player) {
                Player player = (Player) entity;

                if (player.getWorld().getName().equals("ThePit_0")) {
                    if (isDisabledOnPassiveWorld())
                        return false;
                }

                if (damageManager != null) {
                    if (data.getDamageManager().playerIsInCanceledEvent(player))
                        return false;
                }

                if (RegionManager.getInstance().playerIsInRegion(player, RegionManager.RegionType.SPAWN))
                    return false;
            }

            if (entity instanceof Arrow) {
                Arrow arrow = (Arrow) entity;

                if (damageManager != null) {
                    if (damageManager.arrowIsInCanceledEvent(arrow))
                        return false;
                }

                if (RegionManager.getInstance().locationIsInRegion(arrow.getLocation(), RegionManager.RegionType.SPAWN))
                    return false;
            }
        }

        execution.accept(getEnchantLevel(data.getItemSource(), this));

        return true;
    }

    public boolean getAttemptedEnchantExecutionFeedback(ItemStack source) {
        return false;
    }

    public boolean percentChance(int percent) {
        return ThreadLocalRandom.current().nextInt(0, 100) <= percent;
    }

    public boolean itemHasEnchant(ItemStack item, CustomEnchant enchant) {
        if (item == null || item.getType() == Material.AIR)
            return false;

        if (item.getItemMeta().getLore() == null)
            return false;

        List<String> lore = item.getItemMeta().getLore();

        String appendRare = "";

        if (enchant.isRareEnchant())
            appendRare = ChatColor.LIGHT_PURPLE + "RARE! ";

        if (lore.contains(appendRare + ChatColor.BLUE + enchant.getName()))
            return true;

        for (int i = 2; i <= 3; i++) {
            if (lore.contains(appendRare + ChatColor.BLUE + enchant.getName() + " "
                    + romanNumeralConverter.convertToRomanNumeral(i)))
                return true;
        }

        return false;
    }

    public boolean itemHasEnchant(ItemStack item, CustomEnchant enchant, int level) {
        if (item == null || item.getType() == Material.AIR)
            return false;

        if (item.getItemMeta().getLore() == null)
            return false;

        List<String> lore = item.getItemMeta().getLore();

        String appendRare = "";

        if (enchant.isRareEnchant())
            appendRare = ChatColor.LIGHT_PURPLE + "RARE! ";

        if (level == 1)
            return lore.contains(appendRare + ChatColor.BLUE + enchant.getName());

        return lore.contains(appendRare + ChatColor.BLUE + enchant.getName() + " "
                + romanNumeralConverter.convertToRomanNumeral(level));
    }

    public int getEnchantLevel(ItemStack item, CustomEnchant enchant) {
        if (item == null || item.getType() == Material.AIR)
            return 0;

        if (item.getItemMeta().getLore() == null)
            return 0;

        List<String> lore = item.getItemMeta().getLore();

        String appendRare = "";

        if (enchant.isRareEnchant())
            appendRare = ChatColor.LIGHT_PURPLE + "RARE! ";

        if (lore.contains(appendRare + ChatColor.BLUE + enchant.getName()))
            return 1;

        for (int i = 2; i <= 3; i++) {
            if (lore.contains(appendRare + ChatColor.BLUE + enchant.getName() + " "
                    + romanNumeralConverter.convertToRomanNumeral(i)))
                return i;
        }

        return 0;
    }

    public abstract String getName();

    public abstract String getEnchantReferenceName();

    public abstract ArrayList<String> getDescription(int level);

    public abstract boolean isDisabledOnPassiveWorld();

    public abstract EnchantGroup getEnchantGroup();

    public abstract boolean isRareEnchant();

    public abstract Material[] getEnchantItemTypes();
}
