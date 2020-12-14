package com.thebluehats.server.game.managers.combat.templates;

import java.util.function.Function;

import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.enchants.DamageEnchant;
import com.thebluehats.server.game.utils.EntityValidator;

import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public abstract class PostEventTemplate<E extends Event, T> {
    protected final CustomEnchantUtils customEnchantUtils;

    public PostEventTemplate(CustomEnchantUtils customEnchantUtils) {
        this.customEnchantUtils = customEnchantUtils;
    }

    abstract void run(DamageEnchant enchant, E event, Function<PlayerInventory, ItemStack> getSource,
            EntityValidator... validators);
}
