package com.thebluehats.server.game.managers.enchants;

import com.thebluehats.server.game.managers.combat.templates.EventTemplate;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.utils.RomanNumeralConverter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class CustomEnchant<T> implements Listener {
    private final RomanNumeralConverter romanNumeralConverter = new RomanNumeralConverter();

    private EventTemplate[] templates;

    protected CustomEnchant(EventTemplate... templates) {
        this.templates = templates;
    }

    public void runEventTemplates(CustomEnchant enchant, Entity damager, Entity damagee, Function<PlayerInventory, ItemStack> getSource, Consumer<Integer> onSuccess) {
        for (EventTemplate template : templates) {
            template.run(enchant, damager, damagee, getSource, onSuccess);
        }
    }

    public boolean isCompatibleWith(Material material) {
        for (Material mat : getEnchantItemTypes()) {
            if (mat == material) {
                return true;
            }
        }

        return false;
    }

    public boolean canExecuteEnchant(ItemStack source, Entity[] interactedEntities) {
        if (itemHasEnchant(source))
            return calculateConditions(source, interactedEntities, null);

        return false;
    }

    public boolean canExecuteEnchant(ItemStack source, Entity[] interactedEntities, DamageManager damageManager) {
        if (itemHasEnchant(source))
            return calculateConditions(source, interactedEntities, damageManager);

        return false;
    }

    private boolean calculateConditions(ItemStack source, Entity[] interactedEntities, DamageManager damageManager) {
        if (interactedEntities != null) {
            for (Entity entity : interactedEntities) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;

                    if (player.getWorld().getName().equals("ThePit_0")) {
                        if (isDisabledOnPassiveWorld())
                            return false;
                    }

                    if (damageManager != null) {
                        if (damageManager.playerIsInCanceledEvent(player))
                            return false;
                    }

                    // TODO GET RID OF THIS SINGLETON FASTQ!Q!!~!!!
                    // if (RegionManager.getInstance().playerIsInRegion(player,
                    // RegionManager.RegionType.SPAWN))
                    // return false;
                }

                if (entity instanceof Arrow) {
                    Arrow arrow = (Arrow) entity;

                    if (damageManager != null) {
                        if (damageManager.arrowIsInCanceledEvent(arrow))
                            return false;
                    }

                    // if (RegionManager.getInstance().locationIsInRegion(arrow.getLocation(),
                    // RegionManager.RegionType.SPAWN))
                    // return false;
                }
            }
        }

        return true;
    }

    public boolean itemHasEnchant(ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return false;

        if (item.getItemMeta().getLore() == null)
            return false;

        List<String> lore = item.getItemMeta().getLore();

        String appendRare = "";

        if (isRareEnchant())
            appendRare = ChatColor.LIGHT_PURPLE + "RARE! ";

        if (lore.contains(appendRare + ChatColor.BLUE + getName()))
            return true;

        for (int i = 2; i <= 3; i++) {
            if (lore.contains(
                    appendRare + ChatColor.BLUE + getName() + " " + romanNumeralConverter.convertToRomanNumeral(i)))
                return true;
        }

        return false;
    }

    public int getEnchantLevel(ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return 0;

        if (item.getItemMeta().getLore() == null)
            return 0;

        List<String> lore = item.getItemMeta().getLore();

        String appendRare = "";

        if (isRareEnchant())
            appendRare = ChatColor.LIGHT_PURPLE + "RARE! ";

        if (lore.contains(appendRare + ChatColor.BLUE + getName()))
            return 1;

        for (int i = 2; i <= 3; i++) {
            if (lore.contains(
                    appendRare + ChatColor.BLUE + getName() + " " + romanNumeralConverter.convertToRomanNumeral(i)))
                return i;
        }

        return 0;
    }

    public abstract void execute(T args);

    public abstract String getName();

    public abstract String getEnchantReferenceName();

    public abstract ArrayList<String> getDescription(int level);

    public abstract boolean isDisabledOnPassiveWorld();

    public abstract EnchantGroup getEnchantGroup();

    public abstract boolean isRareEnchant();

    public abstract Material[] getEnchantItemTypes();
}
