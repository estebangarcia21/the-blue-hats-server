package me.stevemmmmm.server.game.enchants;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import me.stevemmmmm.server.game.managers.DamageManager;

public class EnchantProcData {
    private ItemStack itemSource;
    private Entity[] interactedEntities;
    private DamageManager damageManager;

    public EnchantProcData(ItemStack source, Entity[] interactedEntities) {
        this.itemSource = source;
        this.interactedEntities = interactedEntities;
    }

    public EnchantProcData(ItemStack source, Entity[] interactedEntities, DamageManager damageManager) {
        this.itemSource = source;
        this.interactedEntities = interactedEntities;
        this.damageManager = damageManager;
    }

    public ItemStack getItemSource() {
        return itemSource;
    }

    public Entity[] getInteractedEntities() {
        return interactedEntities;
    }

    public DamageManager getDamageManager() {
        return damageManager;
    }
}
