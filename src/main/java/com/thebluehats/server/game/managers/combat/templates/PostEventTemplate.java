package com.thebluehats.server.game.managers.combat.templates;

import java.util.function.Function;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.enchants.DamageEnchant;
import com.thebluehats.server.game.utils.EntityValidator;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public abstract class PostEventTemplate {
    private static enum MaterialMatcher {
        LEGGINGS, SWORD, BOW
    }

    private static final ImmutableMap<MaterialMatcher, Function<PlayerInventory, ItemStack>> materialFunctions = ImmutableMap
            .<MaterialMatcher, Function<PlayerInventory, ItemStack>>builder()
            .put(MaterialMatcher.LEGGINGS, PlayerInventory::getLeggings)
            .put(MaterialMatcher.SWORD, PlayerInventory::getItemInMainHand)
            .put(MaterialMatcher.BOW, PlayerInventory::getItemInMainHand).build();

    protected final CustomEnchantUtils customEnchantUtils;

    protected PostEventTemplate(CustomEnchantUtils customEnchantUtils) {
        this.customEnchantUtils = customEnchantUtils;
    }

    protected ImmutableMap<Material, Integer> getItemMap(DamageEnchant enchant, PlayerInventory inventory) {
        Builder<Material, Integer> mapBuilder = ImmutableMap.<Material, Integer>builder();

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

    public abstract void run(DamageEnchant enchant, EntityDamageByEntityEvent event, TargetPlayer targetPlayer,
            EntityValidator... validators);
}
