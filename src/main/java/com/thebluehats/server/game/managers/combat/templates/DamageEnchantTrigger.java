package com.thebluehats.server.game.managers.combat.templates;

import com.google.common.collect.ImmutableMap;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.utils.EntityValidator;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.function.Function;

public abstract class DamageEnchantTrigger {
    private enum MaterialMatcher {
        LEGGINGS, SWORD, BOW
    }

    private static final ImmutableMap<MaterialMatcher, Function<PlayerInventory, ItemStack>> materialFunctions = ImmutableMap
            .<MaterialMatcher, Function<PlayerInventory, ItemStack>>builder()
            .put(MaterialMatcher.LEGGINGS, PlayerInventory::getLeggings)
            .put(MaterialMatcher.SWORD, PlayerInventory::getItemInHand)
            .put(MaterialMatcher.BOW, PlayerInventory::getItemInHand).build();

    protected final CustomEnchantUtils customEnchantUtils;

    protected DamageEnchantTrigger(CustomEnchantUtils customEnchantUtils) {
        this.customEnchantUtils = customEnchantUtils;
    }

    protected ImmutableMap<Material, Integer> getItemMap(DamageTriggeredEnchant enchant, PlayerInventory inventory) {
        ImmutableMap.Builder<Material, Integer> mapBuilder = ImmutableMap.builder();

        for (Material material : enchant.getEnchantItemTypes()) {
            String materialName = material.toString();

            MaterialMatcher[] materialMatchers = MaterialMatcher.values();

            for (MaterialMatcher materialMatcher : materialMatchers) {
                if (materialName.contains(materialMatcher.toString())) {
                    Function<PlayerInventory, ItemStack> getItem = materialFunctions.get(materialMatcher);

                    mapBuilder.put(material, customEnchantUtils.getEnchantLevel(enchant, getItem.apply(inventory)));
                }
            }
        }

        return mapBuilder.build();
    }

    protected boolean inventoryHasEnchant(PlayerInventory inventory, CustomEnchant enchant) {
        return customEnchantUtils.itemHasEnchant(enchant, getItemFunction(enchant).apply(inventory));
    }

    private Function<PlayerInventory, ItemStack> getItemFunction(CustomEnchant enchant) {
        for (Material material : enchant.getEnchantItemTypes()) {
            String materialName = material.toString();

            MaterialMatcher[] materialMatchers = MaterialMatcher.values();

            for (MaterialMatcher materialMatcher : materialMatchers) {
                if (materialName.contains(materialMatcher.toString())) {
                    return materialFunctions.get(materialMatcher);
                }
            }
        }

        return null;
    }

    public abstract void run(DamageTriggeredEnchant enchant, EntityDamageByEntityEvent event, EnchantHolder targetPlayer,
                             EntityValidator... validators);
}